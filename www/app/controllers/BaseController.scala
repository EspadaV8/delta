package controllers

import io.flow.common.v0.models.UserReference
import io.flow.delta.v0.Client
import io.flow.delta.v0.models.Organization
import io.flow.delta.www.lib.{DeltaClientProvider, Section, UiData}
import io.flow.play.controllers._
import play.api.i18n._
import play.api.mvc._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

object Helpers {

  def userFromSession(
    tokenClient: io.flow.token.v0.interfaces.Client,
    session: play.api.mvc.Session
  ) (
    implicit ec: scala.concurrent.ExecutionContext
  ): scala.concurrent.Future[Option[UserReference]] = {
    session.get("user_id") match {
      case None => {
        scala.concurrent.Future { None }
      }
      case Some(userId) => {
        Future { Some(UserReference(id = userId)) }
      }
    }
  }

}

abstract class BaseController(
  val tokenClient: io.flow.token.v0.interfaces.Client,
  val deltaClientProvider: DeltaClientProvider,
  val controllerComponents: ControllerComponents,
  val flowControllerComponents: FlowControllerComponents
) extends FlowController with FlowActionInvokeBlockHelper
  with I18nSupport
{

  private[this] lazy val client = deltaClientProvider.newClient(user = None, requestId = None)

  def section: Option[Section]

  override def unauthorized[A](request: Request[A]): Result = {
    Redirect(routes.LoginController.index(return_url = Some(request.path))).flashing("warning" -> "Please login")
  }

  def withOrganization[T](
    request: IdentifiedRequest[T],
    id: String
  ) (
    f: Organization => Future[Result]
  ) (
    implicit ec: scala.concurrent.ExecutionContext
  ) = {
    deltaClient(request).organizations.get(id = Some(Seq(id)), limit = 1).flatMap { organizations =>
      organizations.headOption match {
        case None => Future {
          Redirect(routes.ApplicationController.index()).flashing("warning" -> s"Organization not found")
        }
        case Some(org) => {
          f(org)
        }
      }
    }
  }

  def organizations[T](
    request: IdentifiedRequest[T]
  ) (
    implicit ec: scala.concurrent.ExecutionContext
  ): Future[Seq[Organization]] = {
    deltaClient(request).organizations.get(
      userId = Some(request.user.id),
      limit = 100
    )
  }

  def uiData[T](
    request: IdentifiedRequest[T]
  ) (
    implicit ec: ExecutionContext
  ): UiData = {
    val user = Await.result(
      deltaClient(request).users.get(id = Some(request.user.id)),
      Duration(1, "seconds")
    ).headOption

    UiData(
      requestPath = request.path,
      user = user,
      section = section
    )
  }

  def uiData[T](
    request: AnonymousRequest[T], user: Option[UserReference]
  ) (
    implicit ec: ExecutionContext
  ): UiData = {
    val userReferenceOption = Await.result(
      Helpers.userFromSession(tokenClient, request.session),
      Duration(1, "seconds")
    )

    val user = userReferenceOption.flatMap { ref =>
      Await.result(
        client.users.get(id = Some(ref.id)),
        Duration(1, "seconds")
      ).headOption
    }

    UiData(
      requestPath = request.path,
      user = user,
      section = section
    )
  }

  def deltaClient[T](request: IdentifiedRequest[T]): Client = {
    deltaClientProvider.newClient(user = Some(request.user), requestId = Some(request.auth.requestId))
  }

}