package org.goldratio.server.coder

import java.nio.ByteOrder

import akka.util.ByteString
import org.goldratio.server.handler.RequestMessage
import org.jboss.netty.buffer.{ ChannelBuffers, ChannelBuffer }
import org.jboss.netty.channel.{ Channel, ChannelHandlerContext }
import org.jboss.netty.handler.codec.frame.FrameDecoder

/**
 * Created by goldratio on 10/24/15.
 */
class MessageDecoder extends FrameDecoder {

  override def decode(context: ChannelHandlerContext, channel: Channel, buffer: ChannelBuffer): AnyRef = {
    if (buffer.readableBytes() < 4) {
      None
    } else {
      val dataLen = buffer.getInt(buffer.readerIndex())

      if (buffer.readableBytes() < dataLen + 4) {
        None
      } else {
        val head = buffer.readInt()
        val bf = ChannelBuffers.dynamicBuffer(head)
        buffer.readBytes(bf, head)

        val command = new String(bf.readBytes(32).array()).trim()
        val id = bf.readInt()
        val content = bf.readBytes(bf.readableBytes()).array()
        new RequestMessage(id, command, content)
      }
    }

  }
}
