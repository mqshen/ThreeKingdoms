package org.goldratio.server.network.handler

import java.net.SocketAddress

import org.goldratio.common.ServerProtocol
import org.goldratio.server.handler.Session
import org.goldratio.server.network.ServerContext
import org.jboss.netty.channel.{ ChannelFuture, Channel }

/**
 * Created by goldratio on 10/25/15.
 */
trait RequestParameter

trait Request {

  def session: Option[Session]

  def ServerContext: ServerContext

  def command: String

  def channel: Channel

  def id: Int

  def sessionId: String

  def getProtocol(): ServerProtocol

  def getRemoteAddress(): SocketAddress

  def content: Array[Byte]

}
trait Response {

  def channel: Channel

  def isWritable: Boolean

  def write(obj: Any): Option[ChannelFuture]

  def getProtocol(): ServerProtocol

  def isChunk: Boolean

}
