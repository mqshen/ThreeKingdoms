package org.goldratio.game.handler

import java.util.concurrent.ConcurrentHashMap

import scala.annotation.tailrec
import scala.collection.JavaConverters._
import scala.collection.concurrent.{ Map => ConcurrentMap }

/**
 * Created by goldratio on 10/25/15.
 */
class RouteRegistry {

  private[this] var _routes = Seq[Route]()

  def prependRoute(route: Route): Unit =
    modifyRoutes(route +: _)

  private def modifyRoutes(f: (Seq[Route] => Seq[Route])): Unit = {
    val oldRoutes = _routes
    _routes = f(oldRoutes)
  }

  def apply(): Seq[Route] = _routes

  private[this] val _statusRoutes: ConcurrentMap[Int, Route] =
    new ConcurrentHashMap[Int, Route].asScala

  /**
   * Return a route for a specific HTTP response status code.
   * @param statusCode the status code.
   *
   */
  def apply(statusCode: Int): Option[Route] = _statusRoutes.get(statusCode)

  private def matchingMethodsExcept(requestPath: String) = {
    _routes.filter(_.apply(requestPath).isDefined)
  }

}
