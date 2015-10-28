package org.goldratio

import org.goldratio.server.handler.ResponseStatus

/**
 * Created by goldratio on 10/26/15.
 */
package object server {

  private[goldratio]type Attributes = {
    def getAttribute(name: String): AnyRef
    def getAttributeNames(): java.util.Enumeration[String]
    def setAttribute(name: String, value: AnyRef): Unit
    def removeAttribute(name: String): Unit
  }

  type ErrorHandler = PartialFunction[Throwable, Any]

}
