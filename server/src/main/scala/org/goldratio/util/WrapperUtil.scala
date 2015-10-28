package org.goldratio.util

import java.io.{ IOException, ByteArrayOutputStream }
import java.util
import java.util.zip.DeflaterOutputStream

import org.goldratio.common.ServerConstants
import org.jboss.netty.buffer.ChannelBuffers

/**
 * Created by goldratio on 10/25/15.
 */
object WrapperUtil {

  val CRLF = "\r\n".getBytes()

  val EMPTY_BYTE = Array.empty[Byte]

  def wrapper(command: String, requestId: Int, body: Array[Byte]) = {
    val bytes = command.getBytes()
    val commandBytes = util.Arrays.copyOf(bytes, 32)

    var bodyBytes = body
    if (ServerConstants.compress) {
      val out = new ByteArrayOutputStream()
      val dis = new DeflaterOutputStream(out)
      try {
        dis.write(body)
        dis.finish()
        dis.close()
        bodyBytes = out.toByteArray()
      } catch {
        case e: IOException =>
          e.printStackTrace()
      } finally {
        if (null != dis) {
          try {
            dis.close()
          } catch {
            case e: IOException =>
          }
        }
      }
    }

    val dataLen = 36 + bodyBytes.length
    val buffer = ChannelBuffers.buffer(dataLen + 4)
    buffer.writeInt(dataLen)
    buffer.writeBytes(commandBytes)
    buffer.writeInt(requestId)
    buffer.writeBytes(bodyBytes)

    buffer
  }

  def wrapper(bytes: Array[Byte]) = {
    ChannelBuffers.wrappedBuffer(bytes)
  }

}
