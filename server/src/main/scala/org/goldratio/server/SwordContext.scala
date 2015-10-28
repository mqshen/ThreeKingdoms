package org.goldratio.server

import org.goldratio.server.handler.{ ResponseStatus, RequestMessage }
import org.goldratio.server.tcp.ResponseMessage

/**
 * Created by goldratio on 10/26/15.
 */
trait SwordContext extends SessionSupport {

  implicit def request: RequestMessage

  implicit def response: ResponseMessage

  /**
   * Gets the status code of the current response.
   */
  def status: Int = response.status.code

  def status_=(code: Int): Unit = { response.status = ResponseStatus(code) }

}
