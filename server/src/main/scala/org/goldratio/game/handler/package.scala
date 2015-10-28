package org.goldratio.game

import org.goldratio.util.MultiMap

/**
 * Created by goldratio on 10/25/15.
 */
package object handler {

  object RouteTransformer {
    implicit def fn2transformer(fn: Route => Route): RouteTransformer = new RouteTransformer {
      override def apply(route: Route): Route = fn(route)
    }
  }

  trait RouteTransformer {
    def apply(route: Route): Route
  }

  type Action = () => Any

  type MultiParams = MultiMap

  val MultiParamsKey = "org.goldratio.MultiParams"
}
