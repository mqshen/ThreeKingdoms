package org.goldratio.server.tcp

import org.goldratio.server.coder.{ MessageEncoder, MessageDecoder }
import org.goldratio.server.network.{ ServerContext, ServerHandler }
import org.goldratio.server.tcp.handler.{ TcpDefaultHandler, FlashPolicyHandler }
import org.jboss.netty.channel.{ Channels, ChannelPipeline, ChannelPipelineFactory }
import org.jboss.netty.handler.execution.{ OrderedMemoryAwareThreadPoolExecutor, ExecutionHandler }

/**
 * Created by goldratio on 10/25/15.
 */
class TcpPipelineFactory(ServerContext: ServerContext, ServerHandler: ServerHandler) extends ChannelPipelineFactory {
  val flashPolicyHandler = new FlashPolicyHandler()
  val executionHandler = new ExecutionHandler(new OrderedMemoryAwareThreadPoolExecutor(4, 0, 0))

  override def getPipeline: ChannelPipeline = {
    val tcpHandler = new TcpDefaultHandler(ServerContext, ServerHandler)
    val pipeline = Channels.pipeline()
    pipeline.addLast("flashPolicy", flashPolicyHandler)
    pipeline.addLast("decoder", new MessageEncoder())
    pipeline.addLast("encoder", new MessageDecoder())
    pipeline.addLast("thread", executionHandler)
    pipeline.addLast("handler", tcpHandler)
    pipeline
  }

}
