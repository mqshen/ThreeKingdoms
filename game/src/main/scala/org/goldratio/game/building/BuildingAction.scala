package org.goldratio.game.building

import org.goldratio.game.ActionBase
import org.goldratio.game.player.{ PlayerResourceAdditionService, PlayerAttributeService, PlayerService }
import org.goldratio.server.{ FAIL, SUCCESS }
import org.goldratio.server.handler.ValidationFormSupport
import org.json4s.JsonAST._

/**
 * Created by goldratio on 10/27/15.
 */
class BuildingAction extends ActionBase with ValidationFormSupport {

  handler("building@getMainCityInfo") {
    playerOnly { player =>
      PlayerAttributeService.getById(player.id) match {
        case Some(playerAttribute) =>
          val list = PlayerResourceAdditionService.getListByPlayerId(player.id)
          val jsonArray = (1 to 5).map { i =>
            val state = getState(i)
            val isOpen = playerAttribute.functionId(state) == '1'

            val (additionMode, additionRate, additionCd) = list.filter(_.resourceType == i).headOption.map { x =>
              (x.additionMode, 2, x.endTime.getTime)
            }.getOrElse((0, 1, 0L))
            JObject(List(
              ("id", JInt(i)), ("isOpen", JBool(isOpen)), ("additionMode", JInt(additionMode)), ("additionRate", JInt(additionRate)), ("additionCd", JInt(additionCd)), ("totalOutput", JArray(List(JObject(List(("type", JInt(i)), ("additionCd", JInt(additionCd)))))))))
          }
          val state = getState(6)
          val isOpen = playerAttribute.functionId(state) == '1'
          val officeJson = (1 to 5).map { i =>
            //TODO
            JObject(List(("type", JInt(i)), ("value", JInt(0))))
          }
          val data = JObject(List(
            ("areas", JArray(jsonArray.toList)), ("id", JInt(6)), ("isOpen", JBool(isOpen)), ("addition", JInt(0)), ("officerOutput", JArray(officeJson.toList)), ("troopLv", JInt(200))))
          SUCCESS("", data)
        case _ =>
          FAIL("")
      }
    }
  }

  private def getState(stateType: Int) = {
    stateType match {
      case 1 => 0
      case 2 => 5
      case 3 => 6
      case 4 => 7
      case 5 => 8
      case 6 => 14
      case _ => 0
    }
  }
}
