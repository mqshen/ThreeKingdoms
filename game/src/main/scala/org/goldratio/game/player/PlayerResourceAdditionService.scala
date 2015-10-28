package org.goldratio.game.player

/**
 * Created by goldratio on 10/28/15.
 */

import java.sql.Date

import org.goldratio.game.BasicTemplate
import org.goldratio.game.player.{ PlayerAttributeService, PlayerAttribute }

import scala.slick.driver.MySQLDriver.simple._

case class PlayerResourceAddition(id: Long, playerId: Long, resourceType: Int, additionMode: Int, endTime: Date, timeType: Int, taskId: Int)

class PlayerResourceAdditionTable(tag: Tag) extends Table[PlayerResourceAddition](tag, "player_resource_addition") with BasicTemplate {
  def playerId = column[Long]("PLAYER_ID")
  def resourceType = column[Int]("RESOURCE_TYPE")
  def additionMode = column[Int]("ADDITION_MODE")
  def endTime = column[Date]("END_TIME")
  def timeType = column[Int]("TIME_TYPE")
  def taskId = column[Int]("TASK_ID")
  def * = (id, playerId, resourceType, additionMode, endTime, timeType, taskId) <> (PlayerResourceAddition.tupled, PlayerResourceAddition.unapply _)
}

object PlayerResourceAdditionService {

  val playerResourceAdditions = TableQuery[PlayerResourceAdditionTable]

  def getListByPlayerId(playId: Long)(implicit s: Session) = {
    playerResourceAdditions.filter(_.playerId === playId).list
  }

}
