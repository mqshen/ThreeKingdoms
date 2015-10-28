package org.goldratio.server.handler

import org.goldratio.server.AttributesMap
import org.jboss.netty.channel.Channel

/**
 * Created by goldratio on 10/24/15.
 */

object Session {

  sealed trait EventType

  object EventType {
    case object CREATE extends EventType
    case object DESTORY extends EventType
    case object ADD extends EventType
    case object REPLACE extends EventType
    case object REMOVE extends EventType
  }

}

trait Session extends AttributesMap {

  val id: String

  def setValid(value: Boolean): Boolean

  def isValid(): Boolean

  def invalidate()

  def markDiscard()

  def access()

  def expire()

  def write(obj: AnyRef)

  var channel: Option[Channel]

}

class SessionEvent(session: Session)

case class SessionAttributeEvent(key: String, value: AnyRef, session: Session) extends SessionEvent(session)