package org.goldratio.server.handler

/**
 * Created by goldratio on 10/26/15.
 */

import org.goldratio.server.tcp.ResponseMessage

import scala.util.DynamicVariable

trait RequestResponseScope {
  /**
   * The currently scoped request.  Valid only inside the `handle` method.
   */
  implicit def request: RequestMessage

  /**
   * The currently scoped response.  Valid only inside the `handle` method.
   */
  implicit def response: ResponseMessage

  protected def withRequestResponse[A](request: RequestMessage, response: ResponseMessage)(f: => A): A

  /**
   * Executes the block with the given request bound to the `request`
   * method.
   */
  protected def withRequest[A](request: RequestMessage)(f: => A): A

  /**
   * Executes the block with the given response bound to the `response`
   * method.
   */
  protected def withResponse[A](response: ResponseMessage)(f: => A): A

}

/**
 * The Scalatra DSL requires a dynamically scoped request and response.
 * This trick is explained in greater detail in Gabriele Renzi's blog
 * post about Step, out of which Scalatra grew:
 *
 * http://www.riffraff.info/2009/4/11/step-a-scala-web-picoframework
 */
trait DynamicScope extends RequestResponseScope {
  /**
   * The currently scoped request.  Valid only inside the `handle` method.
   */
  implicit def request: RequestMessage = dynamicRequest.value

  private[this] val dynamicRequest = new DynamicVariable[RequestMessage](null)

  /**
   * The currently scoped response.  Valid only inside the `handle` method.
   */
  implicit def response: ResponseMessage = dynamicResponse.value

  private[this] val dynamicResponse = new DynamicVariable[ResponseMessage](null)

  protected[goldratio] def withRequestResponse[A](request: RequestMessage, response: ResponseMessage)(f: => A) = {
    withRequest(request) {
      withResponse(response) {
        f
      }
    }
  }

  /**
   * Executes the block with the given request bound to the `request`
   * method.
   */
  protected def withRequest[A](request: RequestMessage)(f: => A) =
    dynamicRequest.withValue(request) {
      f
    }

  /**
   * Executes the block with the given response bound to the `response`
   * method.
   */
  protected def withResponse[A](response: ResponseMessage)(f: => A) =
    dynamicResponse.withValue(response) {
      f
    }

  @deprecated("Do not invoke directly. Use `withRequest` to change the binding, or request to get the value", "2.1.0")
  protected def _request = dynamicRequest

  @deprecated("Do not invoke directly. Use `withResponse` to change the binding, or `response` to get the value", "2.1.0")
  protected def _response = dynamicResponse
}
