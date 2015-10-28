package org.goldratio.game

import java.net.InetSocketAddress
import java.util.concurrent.Executors

import org.goldratio.game.player.PlayerAction
import org.goldratio.game.user.UserAction
import org.goldratio.server.mvc.DispatchHandler
import org.goldratio.server.network.ServerContextImpl
import org.goldratio.server.tcp.TcpPipelineFactory
import org.jboss.netty.bootstrap.ServerBootstrap
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory

import scala.collection.JavaConverters._

/**
 * Created by goldratio on 10/25/15.
 */
object ThreeKingdoms extends scala.App {
  //  implicit val system = ActorSystem.create("ThreeKingdoms-System")
  //
  //  system.actorOf(Props(classOf[ThreeKingdoms]))
  //
  //  system.awaitTermination()

  //  case class Foo(id:Long, name:String)
  //
  //  val map = Map("id" -> 23, "name" -> "sss")
  //
  //  val constructor = classOf[Foo].getConstructors()(0)
  //
  //  constructor.getParameters.foreach { x =>
  //    println(x.getName)
  //  }

  val sb = new ServerBootstrap()
  val factory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool())

  val handler = new DispatchHandler()
  val context = new ServerContextImpl()

  val test = new UserAction
  handler.addHandler("tt", test)

  val play = new PlayerAction
  handler.addHandler("play", play)

  val reflections = new Reflections("my.project")

  sb.setFactory(factory)
  sb.setPipelineFactory(new TcpPipelineFactory(context, handler))
  val options = Map[String, AnyRef]("port" -> new Integer(8100), "maxThreads" -> new Integer(200), "countPerChannel" -> new Integer(5), "tcpHandler" -> "test")
  sb.setOptions(options.asJava)

  sb.bind(new InetSocketAddress(8100))

}
