package org.goldratio.server.tcp

import com.typesafe.scalalogging.Logger
import org.goldratio.common.ServerProtocol
import org.goldratio.server.handler.ResponseStatus
import org.goldratio.server.network.handler.Response
import org.jboss.netty.channel.{ ChannelFutureListener, Channel, ChannelFuture }
import org.slf4j.LoggerFactory

/**
 * Created by goldratio on 10/25/15.
 */
class ResponseMessage(val channel: Channel) extends Response {

  val logger = Logger(LoggerFactory.getLogger(getClass))
  var close = false

  var context: String = _

  /**
   * Note: the servlet API doesn't remember the reason.  If a custom
   * reason was set, it will be returned incorrectly here,
   */
  var status: ResponseStatus = ResponseStatus(1)

  def setStatus(code: Int) = {
    status = ResponseStatus(code)
  }

  override def getProtocol(): ServerProtocol = ServerProtocol.TCP

  override def write(obj: Any): Option[ChannelFuture] = {
    if (channel.isWritable()) {
      val future = channel.write(obj)
      if (close) {
        future.addListener(ChannelFutureListener.CLOSE)
      }
      Some(future)
    } else {
      logger.error("can't write, channel[id:" + channel.getId()
        + ", interestOps:" + channel.getInterestOps()
        + ", bound:" + channel.isBound()
        + ", connected:" + channel.isConnected()
        + ", open:" + channel.isOpen()
        + ", readable:" + channel.isReadable()
        + ", writable:" + channel.isWritable()
        + "]")
      None
    }
  }

  override def isWritable: Boolean = channel.isWritable

  override def isChunk: Boolean = throw new UnsupportedOperationException("tcp response can't offer this operation")
}
