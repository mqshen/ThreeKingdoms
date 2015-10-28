package org.goldratio.server.mvc

import com.typesafe.scalalogging.Logger
import org.goldratio.common.ServerConstants.DatabaseConfig
import org.goldratio.server._
import org.goldratio.server.handler.Handler
import org.goldratio.server.network.ServerHandler
import org.goldratio.server.network.handler.{ Response, Request }
import org.goldratio.server.tcp.{ ResponseMessage, TcpRequest }
import org.slf4j.LoggerFactory

import scala.collection.mutable.ArrayBuffer

/**
 * Created by goldratio on 10/25/15.
 */
class DispatchHandler extends ServerHandler {
  val logger = Logger(LoggerFactory.getLogger(getClass))

  lazy val _servletHandlers: ArrayBuffer[Handler] = ArrayBuffer.empty[Handler]

  def mount(handler: Handler, name: String): Unit = {
    addHandler(name, handler)
  }

  private[this] def pathMapping(urlPattern: String): String = urlPattern match {
    case s if s.endsWith("/*") => s
    case s if s.endsWith("/")  => s + "*"
    case s                     => s + "/*"
  }

  def addHandler(servletName: String, handler: Handler): Unit = {
    _servletHandlers += handler
  }

  override def handler(request: Request, response: Response): Unit = {
    logger.info(request.toString)
    (request, response) match {
      case (req: TcpRequest, rep: ResponseMessage) =>
        Database() withTransaction { session =>
          req.message.databaseSession = session
          //TODO when handler then break
          _servletHandlers.foreach { handler =>
            handler.service(req.message, rep)
          }
        }

    }
  }

}
object Database {

  def apply(): slick.jdbc.JdbcBackend.Database =
    slick.jdbc.JdbcBackend.Database.forURL(DatabaseConfig.url, DatabaseConfig.user, DatabaseConfig.password)

}