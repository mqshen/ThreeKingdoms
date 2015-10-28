package org.goldratio.server

import scala.util.control.NoStackTrace

/**
 * Created by goldratio on 10/26/15.
 */

case class HaltException(status: Option[Int], reason: Option[String], headers: Map[String, String], body: Any)
    extends Throwable with NoStackTrace

class PassException
  extends Throwable
  with NoStackTrace
