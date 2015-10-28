package org.goldratio.server.tcp.handler

import org.goldratio.common.ServerConstants
import org.goldratio.util.WrapperUtil
import org.jboss.netty.buffer.ChannelBuffer
import org.jboss.netty.channel.{ ChannelFutureListener, Channel, ChannelHandlerContext }
import org.jboss.netty.handler.codec.frame.FrameDecoder

/**
 * Created by goldratio on 10/25/15.
 */
class FlashPolicyHandler extends FrameDecoder {
  override def decode(ctx: ChannelHandlerContext, channel: Channel, buffer: ChannelBuffer): AnyRef = {
    if (buffer.readableBytes() < 2) {
      None
    } else {
      val magic1 = buffer.getUnsignedByte(buffer.readerIndex())
      val magic2 = buffer.getUnsignedByte(buffer.readerIndex() + 1)
      val isFlashPolicyRequest = (magic1 == '<' && magic2 == 'p')

      if (isFlashPolicyRequest) {
        buffer.skipBytes(buffer.readableBytes()); // Discard everything
        channel.write(WrapperUtil.wrapper(ServerConstants.CROSSDOMAIN)).addListener(ChannelFutureListener.CLOSE)
        None
      } else {
        ctx.getPipeline().remove(this);
        buffer.readBytes(buffer.readableBytes())
      }
    }
  }
}
