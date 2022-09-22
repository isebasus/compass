package us.isebas.compass.client.pipeline

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import us.isebas.compass.client.util.io.OutByteBuffer

class LengthEncoder(
    private val maxLength: Int,
) : MessageToByteEncoder<ByteArray>() {

    companion object {
        const val NAME = "length_encoder"
    }

    override fun encode(context: ChannelHandlerContext, data: ByteArray, buff: ByteBuf) {
        if (data.size > maxLength) {
            // TODO idk handle something
            println("encoding size exception")
            return
        }

        val prefixed = OutByteBuffer()
        prefixed.writeByteArray(data)
        buff.writeBytes(prefixed.toArray())
    }
}

