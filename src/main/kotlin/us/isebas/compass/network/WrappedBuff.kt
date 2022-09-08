package us.isebas.compass.network

import io.netty.buffer.ByteBuf

class WrappedBuff(private val buffer: ByteBuf) {

    fun wrap(buffer: ByteBuf): WrappedBuff {
        return WrappedBuff(buffer)
    }

    fun readByte(): Byte? {
        return buffer.readByte()
    }

    fun readShort(): Short? {
        return buffer.readShort()
    }

    fun readInt(): Int? {
        return buffer.readInt()
    }

    fun readFloat(): Float? {
        return buffer.readFloat()
    }

    fun readDouble(): Double? {
        return buffer.readDouble()
    }

    fun readLong(): Long? {
        return buffer.readLong()
    }

    fun readBoolean(): Boolean? {
        return buffer.readBoolean()
    }

    fun readString(): String? {
        val length = buffer.readShort()
        val chars = CharArray(length.toInt())
        for (i in 0 until length) {
            chars[i] = buffer.readChar()
        }
        return String(chars)
    }

    fun writeByte(value: Byte): WrappedBuff? {
        buffer.writeByte(value.toInt())
        return this
    }

    fun writeShort(value: Short): WrappedBuff? {
        buffer.writeShort(value.toInt())
        return this
    }

    fun writeInt(value: Int): WrappedBuff? {
        buffer.writeInt(value)
        return this
    }

    fun writeFloat(value: Float): WrappedBuff? {
        buffer.writeFloat(value)
        return this
    }

    fun writeDouble(value: Double): WrappedBuff? {
        buffer.writeDouble(value)
        return this
    }

    fun writeLong(value: Long): WrappedBuff? {
        buffer.writeLong(value)
        return this
    }

    fun writeString(value: String): WrappedBuff? {
        val chars = value.toCharArray()
        buffer.writeShort(chars.size)
        for (c in chars) {
            buffer.writeChar(c.code)
        }
        return this
    }
}