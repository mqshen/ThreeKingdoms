package org.goldratio.server.handler

import org.goldratio.server.tcp.ResponseMessage

/**
 * Created by goldratio on 10/26/15.
 */
trait Handler {
  /**
   * Handles a request and writes to the response.
   */
  def service(request: RequestMessage, res: ResponseMessage): Unit
}
