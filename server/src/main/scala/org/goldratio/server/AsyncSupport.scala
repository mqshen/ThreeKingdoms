package org.goldratio.server

import javax.servlet.AsyncEvent

import org.goldratio.game.handler.{ Route, RouteTransformer, Action }
import org.goldratio.server.handler.{ RequestMessage, HandlerBase }
import org.goldratio.server.tcp.ResponseMessage

/**
 * Created by goldratio on 10/26/15.
 */

object AsyncSupport {
  val ExecutionContextKey = "org.scalatra.ExecutionContext"
}

trait AsyncSupport extends HandlerBase {

  /**
   * Takes a block and converts it to an action that can be run asynchronously.
   */
  protected def asynchronously(f: => Any): Action

  protected def onAsyncEvent(event: AsyncEvent)(thunk: => Any): Unit = {
    withRequest(event.getSuppliedRequest.asInstanceOf[RequestMessage]) {
      withResponse(event.getSuppliedResponse.asInstanceOf[ResponseMessage]) {
        thunk
      }
    }
  }

  protected def withinAsyncContext(context: javax.servlet.AsyncContext)(thunk: => Any): Unit = {
    withRequest(context.getRequest.asInstanceOf[RequestMessage]) {
      withResponse(context.getResponse.asInstanceOf[ResponseMessage]) {
        thunk
      }
    }
  }

  def asyncHandle(transformers: RouteTransformer*)(block: => Any): Route = {
    handler(transformers: _*)(asynchronously(block)())
  }

}
