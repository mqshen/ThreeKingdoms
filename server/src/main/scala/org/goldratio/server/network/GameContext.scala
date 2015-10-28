package org.goldratio.server.network

/**
 * Created by goldratio on 10/25/15.
 */
trait ServerContext {

  def getAttribute(key: String): AnyRef

  def setAttribute(key: String, value: AnyRef): AnyRef

  def removeAttribute(key: String): Boolean

  def invalidate

}
