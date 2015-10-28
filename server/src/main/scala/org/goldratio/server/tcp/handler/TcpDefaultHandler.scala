package org.goldratio.server.tcp.handler

import com.typesafe.scalalogging.Logger
import org.goldratio.server.handler.{ SessionManager, RequestMessage }
import org.goldratio.server.network.{ ServerHandler, ServerContext }
import org.goldratio.server.tcp.{ TcpRequest, ResponseMessage }
import org.jboss.netty.channel._
import org.slf4j.LoggerFactory

/**
 * Created by goldratio on 10/25/15.
 */
trait TcpHandler extends ChannelHandler {

  val serverContext: ServerContext

  val serverHandler: ServerHandler

}

class TcpDefaultHandler(val serverContext: ServerContext, val serverHandler: ServerHandler)
    extends SimpleChannelHandler with TcpHandler {

  val logger = Logger(LoggerFactory.getLogger(getClass))

  //  override def channelClosed(ctx: ChannelHandlerContext, e: ChannelStateEvent) = {
  //    super.channelClosed()
  //  }

  override def messageReceived(ctx: ChannelHandlerContext, e: MessageEvent) = {
    e.getMessage match {
      case message: RequestMessage =>
        message.sessionId = Some(ctx.getAttachment.toString)
        val response = new ResponseMessage(e.getChannel)
        val request = new TcpRequest(ctx, serverContext, e.getChannel, message)
        logger.info("request Message:" + message.toString)
        serverHandler.handler(request, response)
      case _ =>
    }
  }

  override def channelConnected(ctx: ChannelHandlerContext, e: ChannelStateEvent) = {
    Option(ctx.getAttachment()) match {
      case None =>
        val session = SessionManager.getSession(None, true)
        session.channel = Some(e.getChannel)
        ctx.setAttachment(session.id)
      case _ =>
    }
  }

  override def exceptionCaught(ctx: ChannelHandlerContext, e: ExceptionEvent) {
    logger.error("channel error, channel[id:" + e.getChannel.getId
      + ", interestOps:" + e.getChannel.getInterestOps
      + ", bound:" + e.getChannel.isBound
      + ", connected:" + e.getChannel.isConnected
      + ", open:" + e.getChannel.isOpen
      + ", readable:" + e.getChannel.isReadable
      + ", writable:" + e.getChannel.isWritable + "]", e.getCause)
  }

}
