package io.flow.delta.actors.functions

import db.{ImagesDao, TagsDao, UsersDao}
import io.flow.delta.actors.{MainActor, EventLog, SupervisorFunction, SupervisorResult}
import io.flow.postgresql.Authorization
import io.flow.delta.api.lib.GithubUtil
import io.flow.delta.v0.models.{Project, Settings}
import play.api.Logger
import play.libs.Akka
import akka.actor.Actor
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

/**
  * Looks up the latest tag for a project. If found, checks to see if
  * we already have a docker image locally for that tag. If not,
  * ensures that there is a docker image being built for it.
  */
object BuildDockerImage extends SupervisorFunction {

  override def run(
    project: Project
  ) (
    implicit ec: scala.concurrent.ExecutionContext
  ): Future[SupervisorResult] = {
    Future {
      BuildDockerImage(project).run
    }
  }

  override def isEnabled(settings: Settings) = true // TODO: settings.buildDockerImage

}

case class BuildDockerImage(project: Project) extends Github with EventLog {

  def logPrefix = "BuildDockerImage"

  def withProject[T](f: Project => T): Option[T] = {
    Some(f(project))
  }

  private[this] val repo = GithubUtil.parseUri(project.uri).right.getOrElse {
    sys.error(s"Project id[${project.id}] uri[${project.uri}]: Could not parse")
  }

  def run(
    implicit ec: scala.concurrent.ExecutionContext
  ): SupervisorResult = {
    TagsDao.findLatestByProjectId(Authorization.All, project.id) match {
      case None => {
        SupervisorResult.NoChange("Project does not have any tags")
      }

      case Some(tag) => {
        ImagesDao.findByProjectIdAndVersion(project.id, tag.name) match {
          case Some(i) => {
            SupervisorResult.NoChange(s"Image ${repo}:${tag.name} already exist")
          }
          case None => {
            MainActor.ref ! MainActor.Messages.BuildDockerImage(project.id, repo.toString, tag.name)
            waitFor(
              check = {
                log.checkpoint(s"Checking if docker image '${repo}:${tag.name}' is ready")
                !ImagesDao.findByProjectIdAndVersion(project.id, tag.name).isEmpty
              },
              intervalSeconds = 3,
              timeoutSeconds = 10
            ) match {
              case Left(ex) => {
                SupervisorResult.Error(s"Error building image ${repo}:${tag.name}", ex)
              }
              case Right(_) => {
                SupervisorResult.Change(s"Built docker image ${repo}:${tag.name}")
              }
            }
          }
        }
      }
    }
  }

  private[this] def waitFor(
    check: => Boolean,
    intervalSeconds: Int,
    timeoutSeconds: Int
  ): Either[Throwable, Unit] = {
    val startTimeMs = System.currentTimeMillis

    Try {
      while (!check) {
        val durationSeconds = (System.currentTimeMillis - startTimeMs) / 1000
        println(s"durationSeconds[$durationSeconds]")
        if (durationSeconds > timeoutSeconds) {
          sys.error(s"Timeout: Operation took longer than timeout[$timeoutSeconds seconds]")
        } else {
          Thread.sleep(intervalSeconds * 1000)
        }
      }
    } match {
      case Success(_) => Right(())
      case Failure(ex) => Left(ex)
    }
  }

}
