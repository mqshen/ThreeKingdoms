package org.goldratio.server.handler

import org.goldratio.game.handler._
import org.goldratio.game.util.Utils
import scala.collection.JavaConversions._
import scala.collection.immutable.Map
import scala.util.Try

/**
 * Created by goldratio on 10/24/15.
 */
case class RequestMessage(requestId: Int, command: String, content: Array[Byte], var sessionId: Option[String] = None) {

  val params = {
    if (content.length > 0) {
      val str = new String(content).trim()
      str.split("&").toList.map { value =>
        val values = value.split("=")
        val k = Utils.decode(values(0))
        val v = Utils.decode(values(1))
        k -> v
      }(collection.breakOut): Map[String, String]
    } else {
      Map.empty[String, String]
    }
  }

  lazy val session: Session = SessionManager.getSession(sessionId, false)

  var matchdRoute: MatchedRoute = _

  var callbacks: Option[List[(Try[Any]) => Unit]] = None

  var databaseSession: slick.jdbc.JdbcBackend#Session = _

  override def toString = {
    "requestId:" + requestId + "command:" + command + "content:" + params
  }
}

object ResponseStatus {

  def apply(code: Int): ResponseStatus = ResponseStatus(code, ReasonMap.getOrElse(code, ""))
  /**
   * Status code list taken from http://www.iana.org/assignments/http-status-codes/http-status-codes.xml
   */
  private[this] val ReasonMap: Map[Int, String] = Map(
    0 -> "FAIL", 1 -> "SUCCESS", 2 -> "EXCEPTION", 3 -> "PUSH", 4 -> "REDIRECT", 5 -> "BLOCK", 6 -> "CODE", 7 -> "SPECIAL_TIPS")

}
case class ResponseStatus(code: Int, message: String)
    extends Ordered[ResponseStatus] {

  def compare(that: ResponseStatus): Int = code.compareTo(that.code)

  def line: String = {
    val buf = new StringBuilder(message.length + 5)
    buf.append(code)
    buf.append(' ')
    buf.append(message)
    buf.toString()
  }
}