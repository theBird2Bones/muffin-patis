package muffin.model.websocket

import scala.util._

import muffin.model.Post

object domain {

  opaque type RawJson = String

  object RawJson {
    def from(s: String): RawJson = s

    extension (json: RawJson) {
      def value: String = json
    }

  }

  case class Event[A](
      eventType: EventType,
      data: A
  )

  sealed trait EventType {
    def repr: String
  }

  object EventType {

    enum KnownEventType extends EventType {
      case Hello
      case Posted
      case AddedToTeam
      case AuthenticationChallenge
      case ChannelConverted
      case ChannelCreated
      case ChannelDeleted
      case ChannelMemberUpdated
      case ChannelUpdated
      case ChannelViewed
      case ConfigChanged
      case DeleteTeam
      case DirectAdded
      case EmojiAdded
      case EphemeralMessage
      case GroupAdded
      case LeaveTeam
      case LicenseChanged
      case MemberroleUpdated
      case NewUser
      case PluginDisabled
      case PluginEnabled
      case PluginStatusesChanged
      case PostDeleted
      case PostEdited
      case PostUnread
      case PreferenceChanged
      case PreferencesChanged
      case PreferencesDeleted
      case ReactionAdded
      case ReactionRemoved
      case Response
      case RoleUpdated
      case StatusChange
      case Typing
      case UpdateTeam
      case UserAdded
      case UserRemoved
      case UserRoleUpdated
      case UserUpdated
      case DialogOpened
      case ThreadUpdated
      case ThreadFollowChanged
      case ThreadReadChanged

      def repr: String = this.toString
    }

    def fromSnakeCase(s: String): EventType = {
      val tokens = s.split("_").toList.map(_.capitalize)
      Try(
        KnownEventType.valueOf(
          tokens.foldLeft(new StringBuilder(tokens.length)) {
            (builder, token) => builder.addAll(token)
          }
            .result()
        )
      ) match {
        case Success(tpe) => tpe
        case Failure(_)   => CustomEventType(s)
      }
    }

    case class CustomEventType(repr: String) extends EventType
  }

  case class PostedEventData(channelName: String, channelType: ChannelType, senderName: String, post: Post)

  enum ChannelType {
    case Direct
    case Channel
    case Unknown(repr: String)
  }

}
