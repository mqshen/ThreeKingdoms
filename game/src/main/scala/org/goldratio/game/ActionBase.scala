package org.goldratio.game

import org.goldratio.SwordException
import org.goldratio.game.player.Player
import org.goldratio.game.user.User
import org.goldratio.server.handler.HandlerBase

/**
 * Created by goldratio on 10/27/15.
 */
class ActionBase extends HandlerBase {

  def loginOnly(action: => (User) => Any) = { authenticate(action) }

  private def authenticate(action: (User) => Any) = {
    session("user") match {
      case u: User =>
        action(u)
      case _ =>
        throw new SwordException("not ")
    }
  }

  def playerOnly(action: => (Player) => Any) = { playerAuthenticate(action) }

  private def playerAuthenticate(action: (Player) => Any) = {
    session("player") match {
      case u: Player =>
        action(u)
      case _ =>
        throw new SwordException("not ")
    }
  }

}
