package io.flow.delta.actors

import akka.actor.Actor
import db.{BuildsDao, BuildDesiredStatesDao, SettingsDao}
import io.flow.delta.api.lib.StateDiff
import io.flow.delta.v0.models.{Project, Settings, Version}
import io.flow.play.actors.ErrorHandler
import io.flow.postgresql.Authorization
import play.libs.Akka

object ProjectSupervisorActor {

  val StartedMessage = "started PursueDesiredState"
  
  trait Message

  object Messages {
    case class Data(id: String) extends Message

    case class CheckTag(name: String) extends Message
    case object PursueDesiredState extends Message
  }

  val ProjectFunctions = Seq(
    functions.SyncMasterSha,
    functions.TagMaster
  )

  val BuildFunctions = Seq(
    functions.SetDesiredState,
    functions.BuildDockerImage,
    functions.Scale
  )

}

class ProjectSupervisorActor extends Actor with ErrorHandler with DataProject with EventLog {

  private[this] implicit val supervisorActorExecutionContext = Akka.system.dispatchers.lookup("supervisor-actor-context")

  def receive = {

    case msg @ ProjectSupervisorActor.Messages.Data(id) => withVerboseErrorHandler(msg) {
      setProjectId(id)
    }

    case msg @ ProjectSupervisorActor.Messages.PursueDesiredState => withVerboseErrorHandler(msg) {
      withProject { project =>
        val settings = SettingsDao.findByProjectIdOrDefault(Authorization.All, project.id)
        log.message(ProjectSupervisorActor.StartedMessage)
        run(project, settings, ProjectSupervisorActor.ProjectFunctions)

        BuildsDao.findAllByProjectId(Authorization.All, project.id).foreach { build =>
          sender ! MainActor.Messages.BuildSync(build.id)
        }

        log.completed("PursueDesiredState")
      }
    }

    case msg @ ProjectSupervisorActor.Messages.CheckTag(name: String) => withVerboseErrorHandler(msg) {
      withProject { project =>
        BuildsDao.findAllByProjectId(Authorization.All, project.id).map { build =>
          sender ! MainActor.Messages.BuildCheckTag(build.id, name)
        }.toSeq
      }
    }

  }

  /**
    * Sequentially runs through the list of functions. If any of the
    * functions returns a SupervisorResult.Changed or
    * SupervisorResult.Error, returns that result. Otherwise will
    * return NoChange at the end of all the functions.
    */
  private[this] def run(project: Project, settings: Settings, functions: Seq[ProjectSupervisorFunction]) {
    functions.headOption match {
      case None => {
        SupervisorResult.NoChange("All functions returned without modification")
      }
      case Some(f) => {
        isEnabled(settings, f) match {
          case false => {
            log.skipped(format(f, "is disabled in the project settings"))
            run(project, settings, functions.drop(1))
          }
          case true => {
            log.started(format(f))
            f.run(project).map { result =>
              result match {
                case SupervisorResult.Change(desc) => {
                  log.changed(format(f, desc))
                }
                case SupervisorResult.NoChange(desc)=> {
                  log.completed(format(f, desc))
                  run(project, settings, functions.drop(1))
                }
                case SupervisorResult.Error(desc, ex)=> {
                  log.completed(format(f, desc), Some(ex))
                }
              }

            }.recover {
              case ex: Throwable => log.completed(format(f, ex.getMessage), Some(ex))
            }
          }
        }
      }
    }
  }

  private[this] def isEnabled(settings: Settings, f: Any): Boolean = {
    format(f) match {
      case "SyncMasterSha" => settings.syncMasterSha
      case "TagMaster" => settings.tagMaster
      case other => sys.error(s"Cannot determine project setting for function[$other]")
    }
  }

}
