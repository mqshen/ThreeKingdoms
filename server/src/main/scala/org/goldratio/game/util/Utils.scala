package org.goldratio.game.util

import java.io.UnsupportedEncodingException
import java.net.URLDecoder

/**
 * Created by goldratio on 10/25/15.
 */
object Utils {

  def decode(str: String) = {
    try {
      URLDecoder.decode(str, "UTF-8")
    } catch {
      case e: UnsupportedEncodingException =>
        throw e;
      case t: Throwable =>
        str
    }
  }

  def getValue(values: Seq[String], value: String): Seq[String] = {
    if (null == values || values.length == 0) {
      Seq[String](value)
    } else {
      values :+ value
    }
  }

}
