package org.goldratio.server.handler

import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

import akka.actor.Actor
import org.jboss.netty.channel.Channel

import scala.collection.mutable.ArrayBuffer

object SessionManager {

  val sessionListeners = new ArrayBuffer[SessionListener]()

  val sessionAttributeListeners = new ArrayBuffer[SessionAttributeListener]()

  val sessions = new ConcurrentHashMap[String, Session]()

  def generateSessionId(): String = {
    UUID.randomUUID().toString
  }

  def _getSession(sessionId: String): Session = {
    sessions.get(sessionId)
  }

  def getSession(sessionId: Option[String], allowCreate: Boolean): Session = {
    if (allowCreate) {
      val session = sessionId match {
        case Some(sessionId) =>
          _getSession(sessionId)
        case None =>
          val session = new StandardSession(generateSessionId(), sessionListeners, sessionAttributeListeners)
          sessions.put(session.id, session)
          session
      }
      session
    } else {
      _getSession(sessionId.get)
    }
  }

  def removeSession(id: String) = {
    sessions.remove(id)
  }
}