package org.goldratio.game.user

import org.goldratio.forms._
import org.goldratio.game.Constants
import org.goldratio.game.player.PlayerService
import org.goldratio.server.{ SUCCESS, FAIL }
import org.goldratio.server.handler.{ ValidationFormSupport, HandlerBase, RequestMessage }
import org.json4s.JsonAST.{ JString, JObject }

/**
 * Created by goldratio on 10/25/15.
 */
class UserAction extends HandlerBase with ValidationFormSupport {

  case class LoginForm(userName: String, password: String)

  val loginForm = mapping(
    "userName" -> text(minlength(2)), "password" -> text(minlength(2)))(LoginForm.apply)

  handler("login2", loginForm) { form =>
    UserService.login(form.userName, form.password) match {
      case Some(user) =>
        PlayerService.getRoleCount(user.id, Constants.yx) match {
          case count if count <= 0 =>
          case count               =>
        }

        val session = request.session
        session("user") = user
        SUCCESS("", Map("sessionId" -> session.id))
      case _ =>
        FAIL("user not found", Map("reason" -> "test"))
    }
  }

}
