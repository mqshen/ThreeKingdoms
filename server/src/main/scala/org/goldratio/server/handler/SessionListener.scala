package org.goldratio.server.handler

import java.util.EventListener

/**
 * Created by goldratio on 10/24/15.
 */
trait SessionListener extends EventListener {

  def sessionCreated(se: SessionEvent)

  def sessionDestroyed(se: SessionEvent)

}
