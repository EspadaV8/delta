package io.flow.delta.actors.functions

import db.ShasDao
import io.flow.delta.actors.{SupervisorFunction, SupervisorResult}
import io.flow.delta.api.lib.{Email, Semver}
import io.flow.github.v0.models.{TagForm, Tagger, TagSummary}
import io.flow.postgresql.Authorization
import io.flow.delta.api.lib.GithubUtil
import io.flow.delta.v0.models.Project
import org.joda.time.DateTime
import play.api.Logger
import play.libs.Akka
import akka.actor.Actor
import scala.concurrent.Future

/**
  * If there is no tag pointing to the master sha, creates a tag in
  * github and records it here.
  */
object TagIfNeeded extends SupervisorFunction {

  override def run(
    project: Project
  ) (
    implicit ec: scala.concurrent.ExecutionContext
  ): Future[SupervisorResult] = {
    TagIfNeeded(project).run
  }

}

case class TagIfNeeded(project: Project) extends Github {

  private[this] case class Tag(semver: Semver, sha: String)

  private[this] val repo = GithubUtil.parseUri(project.uri).right.getOrElse {
    sys.error(s"Project id[${project.id}] uri[${project.uri}]: Could not parse")
  }

  val InitialTag = "0.0.1"

  def run(
    implicit ec: scala.concurrent.ExecutionContext
  ): Future[SupervisorResult] = {
    ShasDao.findByProjectIdAndMaster(Authorization.All, project.id).map(_.hash) match {

      case None => {
        Future {
          SupervisorResult.NoChange("Shas table does not have an entry for master branch")
        }
      }

      case Some(master) => {
        withGithubClient(project.user.id) { client =>
          client.tags.getTags(repo.owner, repo.project).flatMap { tags =>
            latest(tags) match {
              case None => {
                createTag(InitialTag, master)
              }
              case Some(tag) => {
                Some(tag.sha) == master match {
                  case true => {
                    Future {
                      SupervisorResult.NoChange(s"Latest tag[${tag.semver}] already points to master[${master}]")
                    }
                  }
                  case false => {
                    val nextTag = tag.semver.next.toString
                    createTag(nextTag, master)
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  /**
    * This method actually creates a new tag with the given name,
    * pointing to the specified sha.
    * 
    * @param name e.g. 0.0.2
    * @param sha e.g. ff731cfdad6e5b05ec40535fd7db03c91bbcb8ff
    */
  private[this] def createTag(
    name: String, sha: String
  ) (
      implicit ec: scala.concurrent.ExecutionContext
  ): Future[SupervisorResult] = {
    withGithubClient(project.user.id) { client =>
      client.tags.postGitAndTags(
        repo.owner,
        repo.project,
        TagForm(
          tag = name,
          message = s"Delta automated tag $name",
          `object` = sha,
          tagger = Tagger(
            name = Seq(Email.fromName.first, Email.fromName.last).flatten.mkString(" "),
            email = Email.fromEmail,
            date = new DateTime()
          )
        )
      ).map { result =>
        println("RESULT: " + result)
        SupervisorResult.Change(s"Created tag $name for sha[$sha]")
      }
    }
  }

  /**
    * Given a list of tag summaries from github, returns the latest
    * semver tag along with its sha
    */
  private[this] def latest(tags: Seq[TagSummary]): Option[Tag] = {
    tags.
      flatMap { t =>
        Semver.parse(t.name).map( semvar => Tag(semvar, t.commit.sha) )
      }.
      sortBy { _.semver }.
      reverse.
      headOption
  }

}
