package org.goldratio.server

import javax.servlet.Filter
import javax.servlet.http.HttpServlet

import org.goldratio.server.handler.Handler
import scala.collection.mutable.ArrayBuffer

/**
 * Created by goldratio on 10/26/15.
 */
//class SwordContextHandler {
//
//  lazy val _servletHandlers: ArrayBuffer[Handler] = ArrayBuffer.empty[Handler]
//
//  class RichSwordContext extends AttributesMap {
//
//    override protected def attributes: Attributes = ???
//
//    var _servlets = Array.empty[HandlerHolder]
//
//    def mount(handler: Handler, name: String): Unit = {
//      addHandler(name, handler)
//    }
//
//    private[this] def pathMapping(urlPattern: String): String = urlPattern match {
//      case s if s.endsWith("/*") => s
//      case s if s.endsWith("/") => s + "*"
//      case s => s + "/*"
//    }
//
//    def addHandler(servletName: String, handler: Handler): Unit = {
//      _servletHandlers += handler
//    }
//
//  }
//
//}