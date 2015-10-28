package org.goldratio.game.player

import org.goldratio.event.EventListener
import org.goldratio.server.{ ActionResult, FAIL, SUCCESS }
import org.goldratio.game.{ ActionBase, Constants }
import org.goldratio.game.user.{ User, UserService }
import org.goldratio.forms._
import org.goldratio.server.handler.ValidationFormSupport
import org.goldratio.server.handler.HandlerBase
import org.json4s.JsonAST._
import org.json4s.jackson.JsonMethods._

/**
 * Created by goldratio on 10/27/15.
 */
class PlayerAction extends ActionBase with ValidationFormSupport {

  handler("player@getPlayerList") {
    loginOnly { u =>
      val players = PlayerService.getPlayerList(u.id, "gcld").map(_.toJson())
      val data = JObject(List(
        ("maxRoleNum", JInt(5)), ("playerList", JArray(players)), ("singleRole", JBool(false)), ("maxRole", JInt(5)), ("serverName", JString("test")), ("serverId", JInt(1))))
      SUCCESS("", data)
    }
  }

  case class InfoForm(playerId: Long)

  val infoForm = mapping("playerId" -> long())(InfoForm.apply)

  handler("player@getPlayerInfo", infoForm) { form =>
    loginOnly { u =>
      PlayerService.getPlayerById(form.playerId) match {
        case Some(player) =>
          request.session("player") = player
          //TODO JOIN CHAT
          //joinChatGroup(playerId, request.getSession());

          EventListener(player.id)

          val data = JObject(List(
            ("blackNames", JArray(List.empty)), ("endTime", JInt(0)), ("player", player.toJson())))
          SUCCESS("", data)
        case _ =>
      }
    }
  }

  handler("player@game", infoForm) { form =>
    loginOnly { u =>
      SUCCESS("", Map("msg" -> "true"))
    }
  }

}

