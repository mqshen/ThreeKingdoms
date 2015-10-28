package org.goldratio.server.handler

import com.typesafe.scalalogging.Logger
import org.goldratio.common.ServerConstants
import org.goldratio.forms._
import org.goldratio.game.handler._
import org.goldratio.i18n.I18nSupport
import org.goldratio.server.tcp.ResponseMessage
import org.goldratio.server._
import org.goldratio.util.WrapperUtil
import org.json4s.JsonAST.{ JString, JInt, JObject }
import org.slf4j.LoggerFactory

import scala.annotation.tailrec
import scala.util.{ Success, Try, Failure }

/**
 * Created by goldratio on 10/25/15.
 */
trait HandlerBase extends Handler with SwordContext with I18nSupport with DynamicScope {
  val logger = Logger(LoggerFactory.getLogger(getClass))

  implicit def request: RequestMessage

  implicit def databaseSession = request.databaseSession

  lazy val routes: RouteRegistry = new RouteRegistry

  def callbacks(implicit request: RequestMessage): List[(Try[Any]) => Unit] =
    request.callbacks.getOrElse(List.empty[Try[Any] => Unit])

  protected def isAsyncExecutable(result: Any): Boolean = false

  def onCompleted(fn: Try[Any] => Unit)(implicit request: RequestMessage): Unit = addCallback(fn)

  def addCallback(callback: Try[Any] => Unit)(implicit request: RequestMessage): Unit = {
    request.callbacks = Some(callback :: callbacks)
  }

  def runCallbacks(data: Try[Any])(implicit request: RequestMessage): Unit = {
    callbacks.reverse foreach (_(data))
  }

  protected def addRoute(transformers: Seq[RouteTransformer], action: => Any): Route = {
    val route = Route(transformers, () => action, (req: RequestMessage) => routeBasePath(req))
    routes.prependRoute(route)
    route
  }

  protected def routeBasePath(implicit request: RequestMessage): String = {
    require(request != null, "routeBasePath requires an active request to determine the servlet path")
    request.command
  }

  def renderCallbacks(implicit request: RequestMessage): List[(Try[Any]) => Unit] = {
    request.callbacks.getOrElse(List.empty[Try[Any] => Unit]).asInstanceOf[List[Try[Any] => Unit]]
  }

  def runRenderCallbacks(data: Try[Any])(implicit request: RequestMessage): Unit = {
    renderCallbacks.reverse foreach (_(data))
  }

  def service(request: RequestMessage, response: ResponseMessage): Unit = {
    withRequestResponse(request, response) {
      executeRoutes()
    }
  }

  protected def renderResponse(actionResult: Any): Unit = {
    actionResult match {
      case result: ActionResult =>
        import org.json4s.jackson.JsonMethods._
        val action = JObject(List(("state", JInt(result.status.code)), ("data", result.body)))
        val r = compact(JObject(List(("action", action))))
        val res = WrapperUtil.wrapper(request.command, request.requestId, r.getBytes)
        logger.info("response message:" + request.command + request.requestId + r)
        response.write(res)
      case _ =>
    }
  }

  def requestPath(implicit request: RequestMessage): String = request.command

  protected def runRoutes(routes: Traversable[Route]): Stream[Any] = {
    for {
      route <- routes.toStream // toStream makes it lazy so we stop after match
      matchedRoute <- route.apply(requestPath)
      saved = saveMatchedRoute(matchedRoute)
      actionResult <- invoke(saved)
    } yield actionResult
  }

  private[server] def saveMatchedRoute(matchedRoute: MatchedRoute): MatchedRoute = {
    request.matchdRoute = matchedRoute
    matchedRoute
  }

  protected def invoke(matchedRoute: MatchedRoute): Option[Any] = {
    liftAction(matchedRoute.action)
  }

  private def liftAction(action: Action): Option[Any] = {
    try {
      Some(action())
    } catch {
      case e: PassException => None
    }
  }

  private[this] def handleStatusCode(status: Int): Option[Any] = {
    for {
      handler <- routes(status)
      matchedHandler <- handler(requestPath)
      handlerResult <- invoke(matchedHandler)
    } yield handlerResult
  }

  protected var doNotFound: Action = () => {
    resourceNotFound()
  }

  protected def resourceNotFound()(implicit request: RequestMessage, response: ResponseMessage): Any = {
    response.setStatus(404)
  }

  protected def executeRoutes(): Unit = {
    var result: Any = null
    var rendered = true

    def runActions = {
      val actionResult = runRoutes(routes()).headOption
      // Give the status code handler a chance to override the actionResult
      val r = handleStatusCode(status) getOrElse {
        actionResult getOrElse doNotFound()
      }
      rendered = false
      r
    }

    cradleHalt(result = runActions, e => {
      cradleHalt({
        result = errorHandler(e)
        rendered = false
      }, e => {
        logger.error("", e)
        runCallbacks(Failure(e))
        try {
          renderUncaughtException(e)
        } finally {
          runRenderCallbacks(Failure(e))
        }
      })
    })

    if (!rendered) renderResponse(result)
  }

  protected var errorHandler: ErrorHandler = {
    case t => throw t
  }

  def error(handler: ErrorHandler): Unit = {
    errorHandler = handler orElse errorHandler
  }

  protected def renderUncaughtException(e: Throwable)(
    implicit request: RequestMessage, response: ResponseMessage): Unit = {
    status = 500
  }

  private[this] def cradleHalt(body: => Any, error: Throwable => Any): Any = {
    try body
    catch {
      case e: HaltException => {
        try {
          handleStatusCode(extractStatusCode(e)) match {
            case Some(result) => renderResponse(result)
            case _            => renderHaltException(e)
          }
        } catch {
          case e: HaltException => renderHaltException(e)
          case e: Throwable     => error(e)
        }
      }
      case e: Throwable => error(e)
    }
  }

  protected def renderHaltException(e: HaltException): Unit = {

  }

  protected def extractStatusCode(e: HaltException): Int = e match {
    case HaltException(Some(status), _, _, _) => status
    case _                                    => response.status.code
  }

  def handler(transformers: RouteTransformer*)(action: => Any): Route = addRoute(transformers, action)

  def params(key: String)(implicit request: RequestMessage): String = params(request)(key)

  def params(implicit request: RequestMessage): Map[String, String] = request.params

  /**
   * The current multiparams.  Multiparams are a result of merging the
   * standard request params (query string or post params) with the route
   * parameters extracted from the route matchers of the current route.
   * The default value for an unknown param is the empty sequence.  Invalid
   * outside `handle`.
   */
  //  def multiParams(implicit request: RequestMessage): MultiParams = {
  //    request.params
  //  }
  //
  //  class ScalatraParams(protected val multiMap: Map[String, Seq[String]]) {
  //    def apply(key: String) = {
  //
  //    }
  //  }

  protected implicit def string2RouteMatcher(path: String): RouteMatcher = {
    new SinatraRouteMatcher(path)
  }

}

trait ValidationFormSupport { self: HandlerBase with I18nSupport =>

  def handler[T](path: RouteTransformer, form: ValueType[T])(action: T => Any): Route = {
    handler(path) {
      withValidation(form, params, messages) { obj: T =>
        action(obj)
      }
    }
  }

}
