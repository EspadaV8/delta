/**
 * Generated by apidoc - http://www.apidoc.me
 * Service version: 0.0.3
 * apidoc:0.11.25 http://www.apidoc.me/flow/github/0.0.3/anorm_2_x_parsers
 */
import anorm._

package io.flow.github.v0.anorm.parsers {

  import io.flow.github.v0.anorm.conversions.Standard._

  import io.flow.github.v0.anorm.conversions.Types._

  object ContentsType {

    def parserWithPrefix(prefix: String, sep: String = "_") = parser(s"$prefix${sep}name")

    def parser(name: String = "contents_type"): RowParser[io.flow.github.v0.models.ContentsType] = {
      SqlParser.str(name) map {
        case value => io.flow.github.v0.models.ContentsType(value)
      }
    }

  }

  object Encoding {

    def parserWithPrefix(prefix: String, sep: String = "_") = parser(s"$prefix${sep}name")

    def parser(name: String = "encoding"): RowParser[io.flow.github.v0.models.Encoding] = {
      SqlParser.str(name) map {
        case value => io.flow.github.v0.models.Encoding(value)
      }
    }

  }

  object HookEvent {

    def parserWithPrefix(prefix: String, sep: String = "_") = parser(s"$prefix${sep}name")

    def parser(name: String = "hook_event"): RowParser[io.flow.github.v0.models.HookEvent] = {
      SqlParser.str(name) map {
        case value => io.flow.github.v0.models.HookEvent(value)
      }
    }

  }

  object OwnerType {

    def parserWithPrefix(prefix: String, sep: String = "_") = parser(s"$prefix${sep}name")

    def parser(name: String = "owner_type"): RowParser[io.flow.github.v0.models.OwnerType] = {
      SqlParser.str(name) map {
        case value => io.flow.github.v0.models.OwnerType(value)
      }
    }

  }

  object Visibility {

    def parserWithPrefix(prefix: String, sep: String = "_") = parser(s"$prefix${sep}name")

    def parser(name: String = "visibility"): RowParser[io.flow.github.v0.models.Visibility] = {
      SqlParser.str(name) map {
        case value => io.flow.github.v0.models.Visibility(value)
      }
    }

  }

  object Commit {

    def parserWithPrefix(prefix: String, sep: String = "_") = parser(
      sha = s"$prefix${sep}sha",
      url = s"$prefix${sep}url"
    )

    def parser(
      sha: String = "sha",
      url: String = "url"
    ): RowParser[io.flow.github.v0.models.Commit] = {
      SqlParser.str(sha) ~
      SqlParser.str(url) map {
        case sha ~ url => {
          io.flow.github.v0.models.Commit(
            sha = sha,
            url = url
          )
        }
      }
    }

  }

  object Contents {

    def parserWithPrefix(prefix: String, sep: String = "_") = parser(
      `type` = s"$prefix${sep}type",
      encoding = s"$prefix${sep}encoding",
      size = s"$prefix${sep}size",
      name = s"$prefix${sep}name",
      path = s"$prefix${sep}path",
      content = s"$prefix${sep}content",
      sha = s"$prefix${sep}sha",
      url = s"$prefix${sep}url",
      gitUrl = s"$prefix${sep}git_url",
      htmlUrl = s"$prefix${sep}html_url",
      downloadUrl = s"$prefix${sep}download_url"
    )

    def parser(
      `type`: String = "type",
      encoding: String = "encoding",
      size: String = "size",
      name: String = "name",
      path: String = "path",
      content: String = "content",
      sha: String = "sha",
      url: String = "url",
      gitUrl: String = "git_url",
      htmlUrl: String = "html_url",
      downloadUrl: String = "download_url"
    ): RowParser[io.flow.github.v0.models.Contents] = {
      io.flow.github.v0.anorm.parsers.ContentsType.parser(`type`) ~
      io.flow.github.v0.anorm.parsers.Encoding.parser(encoding) ~
      SqlParser.long(size) ~
      SqlParser.str(name) ~
      SqlParser.str(path) ~
      SqlParser.str(content).? ~
      SqlParser.str(sha) ~
      SqlParser.str(url) ~
      SqlParser.str(gitUrl) ~
      SqlParser.str(htmlUrl) ~
      SqlParser.str(downloadUrl) map {
        case typeInstance ~ encoding ~ size ~ name ~ path ~ content ~ sha ~ url ~ gitUrl ~ htmlUrl ~ downloadUrl => {
          io.flow.github.v0.models.Contents(
            `type` = typeInstance,
            encoding = encoding,
            size = size,
            name = name,
            path = path,
            content = content,
            sha = sha,
            url = url,
            gitUrl = gitUrl,
            htmlUrl = htmlUrl,
            downloadUrl = downloadUrl
          )
        }
      }
    }

  }

  object Error {

    def parserWithPrefix(prefix: String, sep: String = "_") = parser(
      resource = s"$prefix${sep}resource",
      field = s"$prefix${sep}field",
      code = s"$prefix${sep}code"
    )

    def parser(
      resource: String = "resource",
      field: String = "field",
      code: String = "code"
    ): RowParser[io.flow.github.v0.models.Error] = {
      SqlParser.str(resource) ~
      SqlParser.str(field) ~
      SqlParser.str(code) map {
        case resource ~ field ~ code => {
          io.flow.github.v0.models.Error(
            resource = resource,
            field = field,
            code = code
          )
        }
      }
    }

  }

  object GithubObject {

    def parserWithPrefix(prefix: String, sep: String = "_") = parser(
      `type` = s"$prefix${sep}type",
      sha = s"$prefix${sep}sha",
      url = s"$prefix${sep}url"
    )

    def parser(
      `type`: String = "type",
      sha: String = "sha",
      url: String = "url"
    ): RowParser[io.flow.github.v0.models.GithubObject] = {
      SqlParser.str(`type`) ~
      SqlParser.str(sha) ~
      SqlParser.str(url) map {
        case typeInstance ~ sha ~ url => {
          io.flow.github.v0.models.GithubObject(
            `type` = typeInstance,
            sha = sha,
            url = url
          )
        }
      }
    }

  }

  object Hook {

    def parserWithPrefix(prefix: String, sep: String = "_") = parser(
      id = s"$prefix${sep}id",
      url = s"$prefix${sep}url",
      testUrl = s"$prefix${sep}test_url",
      pingUrl = s"$prefix${sep}ping_url",
      name = s"$prefix${sep}name",
      events = s"$prefix${sep}events",
      active = s"$prefix${sep}active",
      configPrefix = s"$prefix${sep}config",
      updatedAt = s"$prefix${sep}updated_at",
      createdAt = s"$prefix${sep}created_at"
    )

    def parser(
      id: String = "id",
      url: String = "url",
      testUrl: String = "test_url",
      pingUrl: String = "ping_url",
      name: String = "name",
      events: String = "events",
      active: String = "active",
      configPrefix: String = "config",
      updatedAt: String = "updated_at",
      createdAt: String = "created_at"
    ): RowParser[io.flow.github.v0.models.Hook] = {
      SqlParser.long(id) ~
      SqlParser.str(url) ~
      SqlParser.str(testUrl) ~
      SqlParser.str(pingUrl) ~
      SqlParser.str(name) ~
      SqlParser.get[Seq[io.flow.github.v0.models.HookEvent]](events) ~
      SqlParser.bool(active) ~
      io.flow.github.v0.anorm.parsers.HookConfig.parserWithPrefix(configPrefix) ~
      SqlParser.get[_root_.org.joda.time.DateTime](updatedAt) ~
      SqlParser.get[_root_.org.joda.time.DateTime](createdAt) map {
        case id ~ url ~ testUrl ~ pingUrl ~ name ~ events ~ active ~ config ~ updatedAt ~ createdAt => {
          io.flow.github.v0.models.Hook(
            id = id,
            url = url,
            testUrl = testUrl,
            pingUrl = pingUrl,
            name = name,
            events = events,
            active = active,
            config = config,
            updatedAt = updatedAt,
            createdAt = createdAt
          )
        }
      }
    }

  }

  object HookConfig {

    def parserWithPrefix(prefix: String, sep: String = "_") = parser(
      url = s"$prefix${sep}url",
      contentType = s"$prefix${sep}content_type"
    )

    def parser(
      url: String = "url",
      contentType: String = "content_type"
    ): RowParser[io.flow.github.v0.models.HookConfig] = {
      SqlParser.str(url).? ~
      SqlParser.str(contentType).? map {
        case url ~ contentType => {
          io.flow.github.v0.models.HookConfig(
            url = url,
            contentType = contentType
          )
        }
      }
    }

  }

  object Ref {

    def parserWithPrefix(prefix: String, sep: String = "_") = parser(
      ref = s"$prefix${sep}ref",
      url = s"$prefix${sep}url",
      objectPrefix = s"$prefix${sep}object"
    )

    def parser(
      ref: String = "ref",
      url: String = "url",
      objectPrefix: String = "object"
    ): RowParser[io.flow.github.v0.models.Ref] = {
      SqlParser.str(ref) ~
      SqlParser.str(url) ~
      io.flow.github.v0.anorm.parsers.GithubObject.parserWithPrefix(objectPrefix) map {
        case ref ~ url ~ objectInstance => {
          io.flow.github.v0.models.Ref(
            ref = ref,
            url = url,
            `object` = objectInstance
          )
        }
      }
    }

  }

  object RefForm {

    def parserWithPrefix(prefix: String, sep: String = "_") = parser(
      ref = s"$prefix${sep}ref",
      sha = s"$prefix${sep}sha"
    )

    def parser(
      ref: String = "ref",
      sha: String = "sha"
    ): RowParser[io.flow.github.v0.models.RefForm] = {
      SqlParser.str(ref) ~
      SqlParser.str(sha) map {
        case ref ~ sha => {
          io.flow.github.v0.models.RefForm(
            ref = ref,
            sha = sha
          )
        }
      }
    }

  }

  object Repository {

    def parserWithPrefix(prefix: String, sep: String = "_") = parser(
      id = s"$prefix${sep}id",
      ownerPrefix = s"$prefix${sep}owner",
      name = s"$prefix${sep}name",
      fullName = s"$prefix${sep}full_name",
      `private` = s"$prefix${sep}private",
      description = s"$prefix${sep}description",
      url = s"$prefix${sep}url",
      htmlUrl = s"$prefix${sep}html_url"
    )

    def parser(
      id: String = "id",
      ownerPrefix: String = "owner",
      name: String = "name",
      fullName: String = "full_name",
      `private`: String = "private",
      description: String = "description",
      url: String = "url",
      htmlUrl: String = "html_url"
    ): RowParser[io.flow.github.v0.models.Repository] = {
      SqlParser.long(id) ~
      io.flow.github.v0.anorm.parsers.User.parserWithPrefix(ownerPrefix) ~
      SqlParser.str(name) ~
      SqlParser.str(fullName) ~
      SqlParser.bool(`private`) ~
      SqlParser.str(description).? ~
      SqlParser.str(url) ~
      SqlParser.str(htmlUrl) map {
        case id ~ owner ~ name ~ fullName ~ privateInstance ~ description ~ url ~ htmlUrl => {
          io.flow.github.v0.models.Repository(
            id = id,
            owner = owner,
            name = name,
            fullName = fullName,
            `private` = privateInstance,
            description = description,
            url = url,
            htmlUrl = htmlUrl
          )
        }
      }
    }

  }

  object Tag {

    def parserWithPrefix(prefix: String, sep: String = "_") = parser(
      tag = s"$prefix${sep}tag",
      sha = s"$prefix${sep}sha",
      url = s"$prefix${sep}url",
      message = s"$prefix${sep}message",
      taggerPrefix = s"$prefix${sep}tagger",
      objectPrefix = s"$prefix${sep}object"
    )

    def parser(
      tag: String = "tag",
      sha: String = "sha",
      url: String = "url",
      message: String = "message",
      taggerPrefix: String = "tagger",
      objectPrefix: String = "object"
    ): RowParser[io.flow.github.v0.models.Tag] = {
      SqlParser.str(tag) ~
      SqlParser.str(sha) ~
      SqlParser.str(url) ~
      SqlParser.str(message) ~
      io.flow.github.v0.anorm.parsers.Tagger.parserWithPrefix(taggerPrefix) ~
      io.flow.github.v0.anorm.parsers.GithubObject.parserWithPrefix(objectPrefix) map {
        case tag ~ sha ~ url ~ message ~ tagger ~ objectInstance => {
          io.flow.github.v0.models.Tag(
            tag = tag,
            sha = sha,
            url = url,
            message = message,
            tagger = tagger,
            `object` = objectInstance
          )
        }
      }
    }

  }

  object TagForm {

    def parserWithPrefix(prefix: String, sep: String = "_") = parser(
      tag = s"$prefix${sep}tag",
      message = s"$prefix${sep}message",
      `object` = s"$prefix${sep}object",
      `type` = s"$prefix${sep}type",
      taggerPrefix = s"$prefix${sep}tagger"
    )

    def parser(
      tag: String = "tag",
      message: String = "message",
      `object`: String = "object",
      `type`: String = "type",
      taggerPrefix: String = "tagger"
    ): RowParser[io.flow.github.v0.models.TagForm] = {
      SqlParser.str(tag) ~
      SqlParser.str(message) ~
      SqlParser.str(`object`) ~
      SqlParser.str(`type`) ~
      io.flow.github.v0.anorm.parsers.Tagger.parserWithPrefix(taggerPrefix) map {
        case tag ~ message ~ objectInstance ~ typeInstance ~ tagger => {
          io.flow.github.v0.models.TagForm(
            tag = tag,
            message = message,
            `object` = objectInstance,
            `type` = typeInstance,
            tagger = tagger
          )
        }
      }
    }

  }

  object TagSummary {

    def parserWithPrefix(prefix: String, sep: String = "_") = parser(
      name = s"$prefix${sep}name",
      commitPrefix = s"$prefix${sep}commit"
    )

    def parser(
      name: String = "name",
      commitPrefix: String = "commit"
    ): RowParser[io.flow.github.v0.models.TagSummary] = {
      SqlParser.str(name) ~
      io.flow.github.v0.anorm.parsers.Commit.parserWithPrefix(commitPrefix) map {
        case name ~ commit => {
          io.flow.github.v0.models.TagSummary(
            name = name,
            commit = commit
          )
        }
      }
    }

  }

  object Tagger {

    def parserWithPrefix(prefix: String, sep: String = "_") = parser(
      name = s"$prefix${sep}name",
      email = s"$prefix${sep}email",
      date = s"$prefix${sep}date"
    )

    def parser(
      name: String = "name",
      email: String = "email",
      date: String = "date"
    ): RowParser[io.flow.github.v0.models.Tagger] = {
      SqlParser.str(name) ~
      SqlParser.str(email) ~
      SqlParser.get[_root_.org.joda.time.DateTime](date) map {
        case name ~ email ~ date => {
          io.flow.github.v0.models.Tagger(
            name = name,
            email = email,
            date = date
          )
        }
      }
    }

  }

  object UnprocessableEntity {

    def parserWithPrefix(prefix: String, sep: String = "_") = parser(
      message = s"$prefix${sep}message",
      errors = s"$prefix${sep}errors"
    )

    def parser(
      message: String = "message",
      errors: String = "errors"
    ): RowParser[io.flow.github.v0.models.UnprocessableEntity] = {
      SqlParser.str(message) ~
      SqlParser.get[Seq[io.flow.github.v0.models.Error]](errors).? map {
        case message ~ errors => {
          io.flow.github.v0.models.UnprocessableEntity(
            message = message,
            errors = errors
          )
        }
      }
    }

  }

  object User {

    def parserWithPrefix(prefix: String, sep: String = "_") = parser(
      id = s"$prefix${sep}id",
      login = s"$prefix${sep}login",
      name = s"$prefix${sep}name",
      email = s"$prefix${sep}email",
      avatarUrl = s"$prefix${sep}avatar_url",
      gravatarId = s"$prefix${sep}gravatar_id",
      url = s"$prefix${sep}url",
      htmlUrl = s"$prefix${sep}html_url",
      `type` = s"$prefix${sep}type"
    )

    def parser(
      id: String = "id",
      login: String = "login",
      name: String = "name",
      email: String = "email",
      avatarUrl: String = "avatar_url",
      gravatarId: String = "gravatar_id",
      url: String = "url",
      htmlUrl: String = "html_url",
      `type`: String = "type"
    ): RowParser[io.flow.github.v0.models.User] = {
      SqlParser.long(id) ~
      SqlParser.str(login) ~
      SqlParser.str(name).? ~
      SqlParser.str(email).? ~
      SqlParser.str(avatarUrl).? ~
      SqlParser.str(gravatarId).? ~
      SqlParser.str(url) ~
      SqlParser.str(htmlUrl) ~
      io.flow.github.v0.anorm.parsers.OwnerType.parser(`type`) map {
        case id ~ login ~ name ~ email ~ avatarUrl ~ gravatarId ~ url ~ htmlUrl ~ typeInstance => {
          io.flow.github.v0.models.User(
            id = id,
            login = login,
            name = name,
            email = email,
            avatarUrl = avatarUrl,
            gravatarId = gravatarId,
            url = url,
            htmlUrl = htmlUrl,
            `type` = typeInstance
          )
        }
      }
    }

  }

  object UserEmail {

    def parserWithPrefix(prefix: String, sep: String = "_") = parser(
      email = s"$prefix${sep}email",
      verified = s"$prefix${sep}verified",
      primary = s"$prefix${sep}primary"
    )

    def parser(
      email: String = "email",
      verified: String = "verified",
      primary: String = "primary"
    ): RowParser[io.flow.github.v0.models.UserEmail] = {
      SqlParser.str(email) ~
      SqlParser.bool(verified) ~
      SqlParser.bool(primary) map {
        case email ~ verified ~ primary => {
          io.flow.github.v0.models.UserEmail(
            email = email,
            verified = verified,
            primary = primary
          )
        }
      }
    }

  }

}