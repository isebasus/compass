package us.isebas.compass.client.util.io

import us.isebas.compass.client.util.lists.collections.HeapArrayByteList
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

open class OutByteBuffer() {
    // Thanks https://gitlab.bixilon.de/bixilon/minosoft/-/blob/master/src/main/java/de/bixilon/minosoft/protocol/protocol/OutByteBuffer.kt
    private val bytes = HeapArrayByteList()

    constructor(buffer: OutByteBuffer) : this() {
        bytes.addAll(buffer.bytes)
    }

    fun writeShort(short: Short) {
        writeShort(short.toInt())
    }

    fun writeShort(short: Int) {
        writeByte(short ushr Byte.SIZE_BITS)
        writeByte(short)
    }

    fun writeInt(int: Int) {
        writeShort(int shr Short.SIZE_BITS)
        writeShort(int)
    }

    open fun writeUnprefixedByteArray(data: ByteArray) {
        bytes.addAll(data)
    }

    open fun writeByteArray(data: ByteArray) {
        writeVarInt(data.size)
        bytes.addAll(data)
    }

    fun writeLong(value: Long) {
        writeInt((value shr Int.SIZE_BITS).toInt())
        writeInt(value.toInt())
    }


    fun writeString(string: String) {
        check(string.length <= 32767) { "String max string length exceeded ${string.length} > ${32767}" }
        val bytes = string.toByteArray(StandardCharsets.UTF_8)
        writeVarInt(bytes.size)
        writeUnprefixedByteArray(bytes)
    }

    fun writeVarLong(long: Long) {
        var value = long
        do {
            var temp = value and 0x7F
            value = value ushr 7
            if (value != 0L) {
                temp = temp or 0x80
            }
            writeByte(temp)
        } while (value != 0L)
    }

    fun writeByte(byte: Byte) {
        bytes.add(byte)
    }

    fun writeByte(byte: Int) {
        writeByte((byte and 0xFF).toByte())
    }

    fun writeByte(long: Long) {
        writeByte((long and 0xFF).toByte())
    }

    fun writeFloat(float: Float) {
        writeInt(float.toBits())
    }

    fun writeFloat(float: Double) {
        writeFloat(float.toFloat())
    }

    fun writeDouble(double: Double) {
        writeLong(double.toBits())
    }

    fun writeDouble(float: Float) {
        writeDouble(float.toDouble())
    }

    fun writeFixedPointNumberInt(double: Double) {
        writeInt((double * 32.0).toInt())
    }

    fun writeVarInt(int: Int) {
        // thanks https://wiki.vg/Protocol#VarInt_and_VarLong
        var value = int
        do {
            var temp = value and 0x7F
            value = value ushr 7
            if (value != 0) {
                temp = temp or 0x80
            }
            writeByte(temp)
        } while (value != 0)
    }

    fun writeBoolean(value: Boolean) {
        writeByte(
            if (value) {
                0x01
            } else {
                0x00
            }
        )
    }

    fun writeUnprefixedString(string: String) {
        writeUnprefixedByteArray(string.toByteArray(StandardCharsets.UTF_8))
    }

    fun toArray(): ByteArray {
        return bytes.toArray()
    }

    fun writeUnprefixedIntArray(data: IntArray) {
        for (i in data) {
            writeInt(i)
        }
    }

    fun writeIntArray(data: IntArray) {
        writeVarInt(data.size)
        writeUnprefixedIntArray(data)
    }

    fun writeUnprefixedLongArray(data: LongArray) {
        for (l in data) {
            writeLong(l)
        }
    }

    fun writeLongArray(data: LongArray) {
        writeVarInt(data.size)
        writeUnprefixedLongArray(data)
    }

    fun writeTo(buffer: ByteBuffer) {
        buffer.put(toArray())
    }

    fun <T> writeArray(array: Array<T>, writer: (T) -> Unit) {
        writeVarInt(array.size)
        for (entry in array) {
            writer(entry)
        }
    }

    fun <T> writeArray(collection: Collection<T>, writer: (T) -> Unit) {
        writeVarInt(collection.size)
        for (entry in collection) {
            writer(entry)
        }
    }

    fun <T> writeOptional(value: T?, writer: (T) -> Unit) {
        writeBoolean(value != null)
        value?.let(writer)
    }
}
