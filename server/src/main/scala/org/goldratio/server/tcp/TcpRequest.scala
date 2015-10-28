package org.goldratio.server.tcp

import java.net.{ SocketAddress, InetSocketAddress }

import org.goldratio.common.ServerProtocol
import org.goldratio.server.handler.{ RequestMessage, Session }
import org.goldratio.server.network.ServerContext
import org.goldratio.server.network.handler.Request
import org.jboss.netty.channel.{ ChannelHandlerContext, Channel }

/**
 * Created by goldratio on 10/25/15.
 */
class TcpRequest(
    val ctx: ChannelHandlerContext, val ServerContext: ServerContext, val channel: Channel, val message: RequestMessage, val command: String = "", val session: Option[Session] = None) extends Request {

  override def sessionId: String = message.sessionId.getOrElse("")

  override def id: Int = message.requestId

  override def getProtocol(): ServerProtocol = ServerProtocol.TCP

  override def content: Array[Byte] = message.content

  override def getRemoteAddress(): SocketAddress = channel.getRemoteAddress()

}
