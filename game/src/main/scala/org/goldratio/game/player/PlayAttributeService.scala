package org.goldratio.game.player

import org.goldratio.game.BasicTemplate
import org.json4s.JsonAST.{ JBool, JInt, JString, JObject }

import scala.slick.driver.MySQLDriver.simple._

/**
 * Created by goldratio on 10/28/15.
 */

case class PlayerAttribute(id: Long, maxStoreNum: String, isAreaNew: String, functionId: String, fullRecruitNum: String, enterCount: String, lastResetTime: String, battleWinTimes: String, battleRewardTimes: String, recruitToken: String, payPoint: String, blackMarketCd: String, intimacy: String, freeConstructionNum: String)

class PlayerAttributeTable(tag: Tag) extends Table[PlayerAttribute](tag, "player_attribute") with BasicTemplate {
  def maxStoreNum = column[String]("MAX_STORE_NUM")
  def isAreaNew = column[String]("IS_AREA_NEW")
  def functionId = column[String]("FUNCTION_ID")
  def fullRecruitNum = column[String]("FULL_RECRUIT_NUM")
  def enterCount = column[String]("ENTER_COUNT")
  def lastResetTime = column[String]("LAST_RESET_TIME")
  def battleWinTimes = column[String]("BATTLE_WIN_TIMES")
  def battleRewardTimes = column[String]("BATTLE_REWARD_TIMES")
  def recruitToken = column[String]("RECRUIT_TOKEN")
  def payPoint = column[String]("PAY_POINT")
  def blackMarketCd = column[String]("BLACK_MARKET_CD")
  def intimacy = column[String]("INTIMACY")
  def freeConstructionNum = column[String]("FREE_CONSTRUCTION_NUM")
  def * = (id, maxStoreNum, isAreaNew, functionId, fullRecruitNum, enterCount, lastResetTime, battleWinTimes, battleRewardTimes, recruitToken, payPoint, blackMarketCd, intimacy, freeConstructionNum) <> (PlayerAttribute.tupled, PlayerAttribute.unapply _)
}

object PlayerAttributeService {

  val playerAttributes = TableQuery[PlayerAttributeTable]

  def getById(id: Long)(implicit s: Session) = {
    playerAttributes.filter(_.id === id).firstOption
  }

}
