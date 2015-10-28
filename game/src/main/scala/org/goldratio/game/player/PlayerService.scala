package org.goldratio.game.player

/**
 * Created by goldratio on 10/27/15.
 */

import java.sql.Date

import org.goldratio.game.BasicTemplate
import org.json4s.JsonAST.{ JBool, JInt, JString, JObject }

import scala.slick.driver.MySQLDriver.simple._

//, sysGold: Int
//, userGold: Int
//, totalUserGold: Int
//, totalTicketGold: Int
//, gm: Int
//def sysGold = column[Int]("SYS_GOLD")
//def userGold = column[Int]("USER_GOLD")
//def totalUserGold = column[Int]("TOTAL_USER_GOLD")
//def totalTicketGold = column[Int]("TOTAL_TICKET_GOLD")
//def gm = column[Int]("GM")

case class Player(id: Long, playerName: String, playerLv: Int, maxLv: Int, userId: Long, consumeLv: Int, yx: String, yxSource: String, forceId: Int, pic: Int, powerId: Int, loginTime: Date, quitTime: Date, dailyOnlineTime: Int, state: Int, deleteTime: Option[Date], createTime: Date, playerServerId: Int, defaultPay: Int, serverNameServerIdPlayerId: String) {
  def toJson(): JObject = {
    JObject(List(
      ("playerId", JInt(id)), ("playerLv", JInt(playerLv)), ("playerName", JString(playerName)), ("lastLoginTime", JInt(loginTime.getTime)), ("pic", JInt(pic)), ("consumLv", JInt(consumeLv)), ("isDelete", JBool(state == 1)), ("forceId", JInt(forceId)), ("defaultPay", JInt(defaultPay))))
  }
}

class PlayerTable(tag: Tag) extends Table[Player](tag, "player") with BasicTemplate {
  def playerName = column[String]("PLAYER_NAME")
  def playerLv = column[Int]("PLAYER_LV")
  def maxLv = column[Int]("MAX_LV")
  def userId = column[Long]("USER_ID")
  def consumeLv = column[Int]("CONSUME_LV")
  def yx = column[String]("YX")
  def yxSource = column[String]("YX_SOURCE")
  def forceId = column[Int]("FORCE_ID")
  def pic = column[Int]("PIC")
  def powerId = column[Int]("POWER_ID")
  def loginTime = column[Date]("LOGIN_TIME")
  def quitTime = column[Date]("QUIT_TIME")
  def dailyOnlineTime = column[Int]("DAILY_ONLINE_TIME")
  def state = column[Int]("DAILY_ONLINE_TIME")
  def deleteTime = column[Option[Date]]("DELETE_TIME")
  def createTime = column[Date]("CREATE_TIME")
  def playerServerId = column[Int]("PLAYER_SERVER_ID")
  def defaultPay = column[Int]("DEFAULT_PAY")
  def serverNameServerIdPlayerId = column[String]("SERVERNAME_SERVERID_PLAYERID")

  def * = (id, playerName, playerLv, maxLv, userId, consumeLv, yx, yxSource, forceId, pic, powerId,
    loginTime, quitTime, dailyOnlineTime, state, deleteTime, createTime, playerServerId,
    defaultPay, serverNameServerIdPlayerId) <> (Player.tupled, Player.unapply _)

}

object PlayerService {

  val players = TableQuery[PlayerTable]

  def create(player: Player)(implicit s: Session) {
    players.insert(player)
  }

  def getRoleCount(userId: Long, yx: String)(implicit s: Session): Int = {
    players.filter(_.userId === userId).filter(_.yx === yx).length.run
  }

  def getPlayerList(userId: Long, yx: String)(implicit s: Session) = {
    players.filter(_.userId === userId).filter(_.yx === yx).list
  }

  def getPlayerById(playId: Long)(implicit s: Session) = {
    players.filter(_.id === playId).firstOption
  }
}