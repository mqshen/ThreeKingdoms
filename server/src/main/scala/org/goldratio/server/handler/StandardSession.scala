package org.goldratio.server.handler

import java.util.concurrent.ConcurrentHashMap

import org.goldratio.server.Attributes
import org.goldratio.server.handler.Session.EventType
import org.jboss.netty.channel.{ ChannelFuture, Channel }
import scala.collection.JavaConverters._

/**
 * Created by goldratio on 10/24/15.
 */

class StandardSession(val id: String, sessionListeners: Seq[SessionListener], sessionAttributeListeners: Seq[SessionAttributeListener], var channel: Option[Channel] = None) extends Session {

  //val attributes = new ConcurrentHashMap[String, AnyRef]().asScala

  val createTime = System.currentTimeMillis()

  var _valid: Boolean = true
  var _expire = false
  var _discard = false

  var lastAccessTime = System.currentTimeMillis()

  override def markDiscard() = _discard = true

  //  override def getAttribute(key: String): AnyRef = {
  //    attributes.get(key)
  //  }
  //
  //  override def setAttribute(key: String, value: AnyRef): Unit = {
  //    Option(attributes.put(key, value)) match {
  //      case Some(obj) =>
  //        attributeNotify(new SessionAttributeEvent(key, obj, this), EventType.REPLACE)
  //      case _ =>
  //        attributeNotify(new SessionAttributeEvent(key, value, this), EventType.ADD)
  //    }
  //  }
  //
  //
  //  override def removeAttribute(key: String): Boolean = {
  //    Option(attributes.remove(key)) match {
  //      case Some(obj) =>
  //        attributeNotify(new SessionAttributeEvent(key, obj, this), EventType.REMOVE)
  //        true
  //      case _ =>
  //        false
  //    }
  //  }

  //TODO wirte message
  override def write(obj: AnyRef) = {

  }

  override def invalidate() = {
    if (_discard) {
      discard()
    } else {
      sessionNotify(EventType.DESTORY)
      SessionManager.removeSession(id)
    }
  }

  def discard() = {
    sessionNotify(EventType.DESTORY)

    SessionManager.removeSession(id)

  }

  override def access() = lastAccessTime = System.currentTimeMillis()

  override def expire() = invalidate()

  def attributeNotify(event: SessionAttributeEvent, eventType: EventType): Unit = {
    sessionAttributeListeners.foreach { listener =>
      eventType match {
        case EventType.ADD     => listener.attributeAdded(event)
        case EventType.REMOVE  => listener.attributeRemoved(event)
        case EventType.REPLACE => listener.attributeRemoved(event)
      }
    }
  }

  def sessionNotify(eventType: EventType) = {
    sessionListeners.foreach { listener =>
      eventType match {
        case EventType.CREATE  => listener.sessionCreated(new SessionEvent(this))
        case EventType.DESTORY => listener.sessionDestroyed(new SessionEvent(this))
      }
    }
  }

  override def setValid(value: Boolean): Boolean = {
    val oldValid = _valid
    _valid = value
    oldValid
  }

  override def isValid(): Boolean = _valid

  override protected val attributes = new RichConcurrentHashMap(new ConcurrentHashMap[String, AnyRef]())
}

class RichConcurrentHashMap(orig: ConcurrentHashMap[String, AnyRef]) {

  def getAttribute(name: String): AnyRef = {
    orig.get(name)
  }

  def getAttributeNames(): java.util.Enumeration[String] = {
    orig.keys()
  }

  def setAttribute(name: String, value: AnyRef): Unit = {
    orig.put(name, value)
  }

  def removeAttribute(name: String): Unit = {
    orig.remove(name)
  }

}
