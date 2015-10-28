package org.goldratio.event

import java.util.concurrent.ConcurrentHashMap

import scala.collection.mutable.{ ListBuffer, HashMap }

/**
 * Created by goldratio on 10/27/15.
 */
trait Event {
  def sendPlayerId: Long
  def receivePlayerIds: Array[Long]
  def id: Int
  def content: AnyRef
  def timestamp: Long

  var delayNum: Int
  def delayNum_(value: Int) = delayNum = value

  def eventType: PushEventType
}

sealed trait PushEventType

object PushEventType {
  case object ALL_PLAYER_IDE extends PushEventType
  case object EVENT_TYPE_COMMON extends PushEventType
  case object EVENT_TYPE_DELAY extends PushEventType
  case object EVENT_TYPE_DELAY_TIME extends PushEventType
}

object EventListener {
  import org.goldratio.event.PushEventType._

  val pipe = new ConcurrentHashMap[Long, ListBuffer[Event]]()

  def apply(playerId: Long) = {
    if (pipe.containsKey(playerId)) {
      pipe.get(playerId)
    } else {
      val eventList = ListBuffer.empty[Event]
      Option(pipe.putIfAbsent(playerId, eventList)) match {
        case Some(list) => list
        case _          => eventList
      }

    }
  }

  def getEvent(playerId: Long): HashMap[Int, Event] = {
    Option(pipe.get(playerId)) match {
      case Some(list) if list.size > 0 =>
        list.synchronized {
          val resultMap = new HashMap[Int, Event]()
          var size = list.size
          while (size > 0) {
            val event = list.remove(0)
            size -= 1
            event.eventType match {
              case EVENT_TYPE_COMMON =>
                resultMap.put(event.id, event)
              case EVENT_TYPE_DELAY =>
                if (event.delayNum == 0) {
                  resultMap.put(event.id, event)
                } else {
                  event.delayNum = event.delayNum - 1
                  list += event
                }
              case EVENT_TYPE_DELAY_TIME =>
                if (event.timestamp <= System.currentTimeMillis()) {
                  resultMap.put(event.id, event)
                } else {
                  list += event
                }
            }
          }
          resultMap
        }
      case _ =>
        HashMap.empty[Int, Event]
    }

  }
}
