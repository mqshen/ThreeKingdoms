package org.goldratio.common

/**
 * Created by goldratio on 10/25/15.
 */
sealed trait ServerProtocol

object ServerProtocol {
  case object HTTP extends ServerProtocol
  case object TCP extends ServerProtocol
}
