package org.goldratio.game.building

/**
 * Created by goldratio on 10/27/15.
 */
import org.goldratio.game.BasicTemplate
import org.goldratio.game.player.{ PlayerResourceAdditionService, PlayerAttributeService, PlayerAttribute }
import org.json4s.JsonAST._

import scala.slick.driver.MySQLDriver.simple._

case class Building(id: Long, playerId: Long, buildingId: String, lv: String, areaId: String, outputType: String, updateTime: String, state: String, isNew: String, eventId: String, speedUpNum: String)

class BuildingTable(tag: Tag) extends Table[Building](tag, "PlayerBuilding") with BasicTemplate {
  def playerId = column[Long]("PLAYER_ID")
  def buildingId = column[String]("BUILDING_ID")
  def lv = column[String]("LV")
  def areaId = column[String]("AREA_ID")
  def outputType = column[String]("OUTPUT_TYPE")
  def updateTime = column[String]("UPDATE_TIME")
  def state = column[String]("STATE")
  def isNew = column[String]("IS_NEW")
  def eventId = column[String]("EVENT_ID")
  def speedUpNum = column[String]("SPEED_UP_NUM")

  def * = (id, playerId, buildingId, lv, areaId, outputType, updateTime, state, isNew, eventId, speedUpNum) <>
    (Building.tupled, Building.unapply _)
}

object BuildingService {

  val buildings = TableQuery[BuildingTable]

  def getMainCity(playerId: Long)(implicit s: Session) = {
    //TODO
  }

}

