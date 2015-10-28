package org.goldratio.server.network

import java.util.concurrent.ConcurrentHashMap

/**
 * Created by goldratio on 10/25/15.
 */
class ServerContextImpl extends ServerContext {
  val map = new ConcurrentHashMap[String, AnyRef]()

  def getAttribute(key: String): AnyRef = {
    map.get(key)
  }

  def setAttribute(key: String, value: AnyRef): AnyRef = {
    map.put(key, value)
  }

  def removeAttribute(key: String): Boolean = {
    map.remove(key) != null
  }

  def invalidate {
    map.clear
  }
}
