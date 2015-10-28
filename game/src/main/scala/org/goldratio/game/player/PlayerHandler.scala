package org.goldratio.game.player

import org.goldratio.game.handler.{ Message, GameMessageHandler }
import org.goldratio.game.user.User

/**
 * Created by goldratio on 10/27/15.
 */

sealed trait Action

object Action {
  case object LOGINE extends Action
  case object LOGINOUT extends Action
  case object REGISTER extends Action
}

case class LoginMessage(player: Player, action: Action, user: User) extends Message {
}
class LoginMessageHandler extends GameMessageHandler {

  override def handler(message: Message): Unit = {
    message match {
      case m: LoginMessage =>
        m.action match {
          case Action.LOGINE =>
            handlerLogin(m)
          case Action.LOGINE =>
            handlerLoginOut(m)
          case Action.LOGINE =>
            handlerRegister(m)
        }
      case _ =>
    }
  }

  def handlerLogin(message: LoginMessage): Unit = {

  }

  def handlerLoginOut(message: LoginMessage): Unit = {

  }

  def handlerRegister(message: LoginMessage): Unit = {

  }
}
