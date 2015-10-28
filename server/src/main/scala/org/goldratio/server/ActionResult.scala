package org.goldratio.server

import org.goldratio.server.handler.ResponseStatus
import org.json4s.JsonAST.{ JNull, JValue }

import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._

/**
 * Created by goldratio on 10/27/15.
 */
case class ActionResult(status: ResponseStatus, body: JValue)

private object ActionResultHelpers {

  def responseStatus(status: Int, reason: String): ResponseStatus = {
    reason match {
      case "" | null => ResponseStatus(status)
      case _         => new ResponseStatus(status, reason)
    }
  }
}
import ActionResultHelpers._

object FAIL {
  def apply(msg: String, value: Map[String, String] = Map.empty[String, String]) = {
    ActionResult(responseStatus(0, msg), render(value))
  }
}

object SUCCESS {
  def apply(msg: String, value: Map[String, String]) = {
    ActionResult(responseStatus(1, msg), render(value))
  }

  def apply(msg: String, value: JValue) = {
    ActionResult(responseStatus(1, msg), value)
  }
}
