package org.goldratio.server.handler

import akka.actor.Actor
import akka.actor.Actor.Receive
import org.goldratio.server.handler.Session.EventType

/**
 * Created by goldratio on 10/24/15.
 */
trait SessionAttributeListener {

  def attributeAdded(se: SessionAttributeEvent)

  def attributeRemoved(se: SessionAttributeEvent)

  def attributeReplaced(se: SessionAttributeEvent)

}
