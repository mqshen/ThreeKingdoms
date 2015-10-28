package org.goldratio.game.handler

import org.goldratio.server.handler.RequestMessage
import org.goldratio.util.MultiMap

/**
 * Created by goldratio on 10/25/15.
 */
case class Route(routeMatchers: Seq[RouteMatcher] = Seq.empty,
                 action: Action,
                 contextPath: RequestMessage => String = _ => "",
                 metadata: Map[Symbol, Any] = Map.empty) {
  /**
   * Optionally returns this route's action and the multi-map of route
   * parameters extracted from the matchers.  Each matcher's returned params
   * are merged into those of the previous.  If any matcher returns None,
   * None is returned.  If there are no route matchers, some empty map is
   * returned.
   */
  def apply(requestPath: String): Option[MatchedRoute] = {
    routeMatchers.foldLeft(Option(MultiMap())) {
      (acc: Option[MultiParams], routeMatcher: RouteMatcher) =>
        for {
          routeParams <- acc
          matcherParams <- routeMatcher(requestPath)
        } yield routeParams ++ matcherParams
    } map { routeParams => MatchedRoute(action, routeParams) }
  }

  /**
   * The reversible matcher of a route is the first reversible matcher, if
   * any.  This matcher may be used to generate URIs.
   */
  lazy val reversibleMatcher: Option[RouteMatcher] =
    routeMatchers find (_.isInstanceOf[ReversibleRouteMatcher])

  /**
   * Determines whether this is a reversible route.
   */
  lazy val isReversible: Boolean = !reversibleMatcher.isEmpty

  override def toString: String = routeMatchers mkString " "
}

object Route {

  def apply(transformers: Seq[RouteTransformer], action: Action): Route =
    apply(transformers, action, (_: RequestMessage) => "")

  def apply(transformers: Seq[RouteTransformer], action: Action, contextPath: RequestMessage => String): Route = {
    val route = Route(action = action, contextPath = contextPath)
    transformers.foldLeft(route) { (route, transformer) => transformer(route) }
  }

  def appendMatcher(matcher: RouteMatcher): RouteTransformer = { (route: Route) =>
    route.copy(routeMatchers = route.routeMatchers :+ matcher)
  }

}

case class MatchedRoute(action: Action, multiParams: MultiParams)
