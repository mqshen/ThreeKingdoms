package org.goldratio.game.handler

import org.goldratio.common.SinatraPathPatternParser

import scala.util.parsing.combinator.RegexParsers
import org.goldratio.util.RicherString._

/**
 * Created by goldratio on 10/25/15.
 */
trait RouteMatcher extends RouteTransformer {

  def apply(requestPath: String): Option[MultiParams]

  def apply(route: Route): Route = Route.appendMatcher(this)(route)

}

trait ReversibleRouteMatcher {

  /**
   * Generates a URI from a route matcher.
   *
   * @param params a map of named params extractable by the route
   * @param splats a list of splats extractable by the route
   * @return a String that would match the route with the given params and splats
   */
  def reverse(params: Map[String, String], splats: List[String]): String

}

final class SinatraRouteMatcher(pattern: String)
    extends RouteMatcher
    with ReversibleRouteMatcher {

  lazy val generator: (Builder => Builder) = BuilderGeneratorParser(pattern)

  def apply(requestPath: String): Option[MultiParams] = SinatraPathPatternParser(pattern)(requestPath)

  def reverse(params: Map[String, String], splats: List[String]): String =
    generator(Builder("", params, splats)).get

  case class Builder(path: String, params: Map[String, String], splats: List[String]) {

    def addLiteral(text: String): Builder = copy(path = path + text)

    def addSplat: Builder = copy(path = path + splats.head, splats = splats.tail)

    def addNamed(name: String): Builder =
      if (params contains name) copy(path = path + params(name), params = params - name)
      else throw new Exception("Builder \"%s\" requires param \"%s\"" format (pattern, name))

    def addOptional(name: String): Builder =
      if (params contains name) copy(path = path + params(name), params = params - name)
      else this

    def addPrefixedOptional(name: String, prefix: String): Builder =
      if (params contains name) copy(path = path + prefix + params(name), params = params - name)
      else this

    // checks all splats are used, appends additional params as a query string
    def get: String = {
      if (!splats.isEmpty) throw new Exception("Too many splats for builder \"%s\"" format pattern)
      val pairs = params map { case (key, value) => key.urlEncode + "=" + value.urlEncode }
      val queryString = if (pairs.isEmpty) "" else pairs.mkString("?", "&", "")
      path + queryString
    }
  }

  object BuilderGeneratorParser extends RegexParsers {

    def apply(pattern: String): (Builder => Builder) = parseAll(tokens, pattern) get

    private def tokens: Parser[Builder => Builder] = rep(token) ^^ {
      tokens => tokens reduceLeft ((acc, fun) => builder => fun(acc(builder)))
    }

    private def token: Parser[Builder => Builder] = splat | prefixedOptional | optional | named | literal

    private def splat: Parser[Builder => Builder] = "*" ^^^ { builder => builder addSplat }

    private def prefixedOptional: Parser[Builder => Builder] =
      ("." | "/") ~ "?:" ~ """\w+""".r ~ "?" ^^ {
        case p ~ "?:" ~ o ~ "?" => builder => builder addPrefixedOptional (o, p)
      }

    private def optional: Parser[Builder => Builder] =
      "?:" ~> """\w+""".r <~ "?" ^^ { str => builder => builder addOptional str }

    private def named: Parser[Builder => Builder] =
      ":" ~> """\w+""".r ^^ { str => builder => builder addNamed str }

    private def literal: Parser[Builder => Builder] =
      ("""[\.\+\(\)\$]""".r | ".".r) ^^ { str => builder => builder addLiteral str }
  }

  override def toString = pattern

}
