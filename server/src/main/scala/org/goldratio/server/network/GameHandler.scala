package org.goldratio.server.network

import org.goldratio.server.network.handler.{ Response, Request }

/**
 * Created by goldratio on 10/25/15.
 */
trait ServerHandler {

  def handler(request: Request, response: Response)

}
