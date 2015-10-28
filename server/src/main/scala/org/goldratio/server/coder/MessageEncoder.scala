package org.goldratio.server.coder

import org.jboss.netty.channel.{ Channel, ChannelHandlerContext }
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder

/**
 * Created by goldratio on 10/25/15.
 */
class MessageEncoder extends OneToOneEncoder {
  override def encode(channelHandlerContext: ChannelHandlerContext, channel: Channel, o: AnyRef): AnyRef = o
}
