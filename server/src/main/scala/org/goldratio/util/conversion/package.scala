package org.goldratio.util

import scala.annotation.implicitNotFound

/**
 * Created by goldratio on 10/26/15.
 */
package object conversion {

  @implicitNotFound(msg = "Cannot find a TypeConverter type class from ${S} to ${T}")
  trait TypeConverter[S, T] { def apply(s: S): Option[T] }

}
