package us.isebas.compass.client.pipeline

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder

class LengthDecoder(
    private val maxLength: Int,
) : ByteToMessageDecoder() {

    companion object {
        const val NAME = "length_decoder"

        private fun ByteBuf.readVarInt(): Int {
            var readCount = 0
            var varInt = 0
            var currentByte: Int
            do {
                if (this.readableBytes() <= 0) {
                    throw BufferTooShortException()
                }
                currentByte = this.readByte().toInt()
                val value = currentByte and 0x7F
                varInt = varInt or (value shl 7 * readCount)
                readCount++
                if (readCount > 5) {
                    throw RuntimeException("VarInt is too big")
                }
            } while (currentByte and 0x80 != 0)

            return varInt
        }

        private class BufferTooShortException : Exception()
    }


    override fun decode(ctx: ChannelHandlerContext, buffer: ByteBuf, out: MutableList<Any>) {
        buffer.markReaderIndex()
        if (buffer.readableBytes() < 2) {
            buffer.resetReaderIndex()
            return
        }
        val length: Int
        try {
            length = buffer.readVarInt()
        } catch (error: BufferTooShortException) {
            buffer.resetReaderIndex()
            return
        }

        if (length > maxLength) {
            // TODO handle this in some way
            return
        }

        if (buffer.readableBytes() < length) {
            buffer.resetReaderIndex()
            return
        }

        val array = ByteArray(length)
        buffer.readBytes(array)

        out += array

    }

}