package org.goldratio.server

import javax.servlet.http.{ HttpSession, HttpServletRequest }

import org.goldratio.server.handler.{ RequestMessage, Session }

/**
 * Created by goldratio on 10/26/15.
 */
trait SessionSupport {

  /**
   * The current session.  Creates a session if none exists.
   */
  implicit def session(implicit request: RequestMessage): Session = request.session

  def session(key: String)(implicit request: RequestMessage): Any = session(request)(key)

  def session(key: Symbol)(implicit request: RequestMessage): Any = session(request)(key)

  /**
   * The current session.  If none exists, None is returned.
   */
  def sessionOption(implicit request: RequestMessage): Option[Session] = Option(request.session)

}
