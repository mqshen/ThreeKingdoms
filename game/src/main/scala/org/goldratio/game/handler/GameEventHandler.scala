package org.goldratio.game.handler

/**
 * Created by goldratio on 10/27/15.
 */
trait Message

trait GameMessageHandler {

  def handler(message: Message)

}
