package org.goldratio.i18n

import java.util.Locale
import javax.servlet.http.HttpServletRequest
import org.goldratio.server.handler.{ HandlerBase, RequestMessage }

object I18nSupport {

  val LocaleKey: String = "org.goldratio.i18n.locale"

  val UserLocalesKey: String = "org.goldratio.i18n.userLocales"

  val MessagesKey: String = "messages"

}

trait I18nSupport { this: HandlerBase =>

  import org.goldratio.i18n.I18nSupport._

  def messages(key: String)(implicit request: RequestMessage): String = messages(request)(key)

  def messages(implicit request: RequestMessage): Messages = {
    if (request == null) {
      throw new Exception("There needs to be a request in scope to call messages")
    } else {
      request.params.get(MessagesKey).map(_.asInstanceOf[Messages]).getOrElse {
        Messages()
      }
    }
  }

  /**
   * Provides a default Message resolver
   *
   * @param locale Locale used to create instance
   * @return a new instance of Messages, override to provide own implementation
   */
  def provideMessages(locale: Locale): Messages = Messages(locale)

  /**
   * Reads a locale from a String
   * @param in a string like en_GB or de_DE
   */
  private def localeFromString(in: String): Locale = {
    val token = in.split("_")
    new Locale(token.head, token.last)
  }

  private def defaultLocale: Locale = Locale.getDefault

}

