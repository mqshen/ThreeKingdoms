package org.goldratio.game

/**
 * Created by goldratio on 10/27/15.
 */
import scala.slick.driver.MySQLDriver.simple._

trait BasicTemplate { self: Table[_] =>
  val id = column[Long]("ID")

  def byId(queryId: Long) = (id === queryId.bind)
}
