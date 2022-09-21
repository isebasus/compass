package us.isebas.compass.client.util.io

import java.nio.charset.StandardCharsets

open class InByteBuffer {
    private val bytes: ByteArray
    var pointer = 0

    constructor(bytes: ByteArray) {
        this.bytes = bytes
    }

    constructor(buffer: InByteBuffer) {
        bytes = buffer.bytes.clone()
        pointer = buffer.pointer
    }

    val size: Int
        get() = bytes.size

    val bytesLeft: Int
        get() = size - pointer

    fun readByte(): Byte {
        // TODO handle if pointer >= size
        return bytes[pointer++]
    }

    open fun readByteArray(length: Int = readVarInt()): ByteArray {
        check(length <= bytes.size) { "Trying to allocate too much memory!" }
        val array = ByteArray(length)
        System.arraycopy(bytes, pointer, array, 0, length)
        pointer += length
        return array
    }

    fun readUnsignedByte(): Int {
        return readByte().toInt() and ((1 shl Byte.SIZE_BITS) - 1)
    }


    fun readShort(): Short {
        return (readUnsignedByte() shl Byte.SIZE_BITS or readUnsignedByte()).toShort()
    }

    fun readShortArray(length: Int = readVarInt()): ShortArray {
        check(length <= bytes.size / Short.SIZE_BYTES) { "Trying to allocate too much memory!" }
        val array = ShortArray(length)
        for (i in 0 until length) {
            array[i] = readShort()
        }
        return array
    }

    fun readUnsignedShort(): Int {
        return readShort().toInt() and ((1 shl Short.SIZE_BITS) - 1)
    }

    fun readInt(): Int {
        return (readUnsignedShort() shl Short.SIZE_BITS or readUnsignedShort())
    }

    fun readIntArray(length: Int = readVarInt()): IntArray {
        check(length <= bytes.size / Int.SIZE_BYTES) { "Trying to allocate too much memory!" }
        val array = IntArray(length)
        for (i in 0 until length) {
            array[i] = readInt()
        }
        return array
    }

    fun readUnsignedInt(): Long {
        return readInt().toLong() and ((1L shl Int.SIZE_BITS) - 1)
    }


    fun readVarInt(): Int {
        var byteCount = 0
        var result = 0
        var read: Int
        do {
            read = readUnsignedByte()
            result = result or (read and 0x7F shl (Byte.SIZE_BITS - 1) * byteCount)
            byteCount++
            require(byteCount <= Int.SIZE_BYTES + 1) { "VarInt is too big" }
        } while (read and 0x80 != 0)

        return result
    }


    @JvmOverloads
    fun readVarIntArray(length: Int = readVarInt()): IntArray {
        check(length <= bytes.size) { "Trying to allocate too much memory!" }
        val array = IntArray(length)
        for (i in 0 until length) {
            array[i] = readVarInt()
        }
        return array
    }

    fun readUnsignedVarInt(): Long {
        return readVarInt().toLong() and ((1 shl Int.SIZE_BITS) - 1).toLong()
    }


    fun readLong(): Long {
        return (readUnsignedInt() shl Int.SIZE_BITS or readUnsignedInt())
    }

    fun readLongArray(length: Int = readVarInt()): LongArray {
        check(length <= bytes.size / Long.SIZE_BYTES) { "Trying to allocate too much memory!" }
        val array = LongArray(length)
        for (i in 0 until length) {
            array[i] = readLong()
        }
        return array
    }

    fun readLongArray(target: LongArray, size: Int = readVarInt()) {
        for (i in 0 until size) {
            target[i] = readLong()
        }
    }


    fun readVarLong(): Long {
        var byteCount = 0
        var result = 0L
        var read: Int
        do {
            read = readUnsignedByte()
            result = result or ((read and 0x7F shl (Byte.SIZE_BITS - 1) * byteCount).toLong())
            byteCount++
            require(byteCount <= Long.SIZE_BYTES + 1) { "VarLong is too big" }
        } while (read and 0x80 != 0)

        return result
    }

    fun readVarLongArray(length: Int = readVarInt()): LongArray {
        check(length <= bytes.size) { "Trying to allocate too much memory!" }
        val array = LongArray(length)
        for (i in 0 until length) {
            array[i] = readVarLong()
        }
        return array
    }

    fun readFloat(): Float {
        return Float.fromBits(readInt())
    }

    fun readFloatArray(length: Int = readVarInt()): FloatArray {
        check(length <= bytes.size / Float.SIZE_BYTES) { "Trying to allocate too much memory!" }
        val array = FloatArray(length)
        for (i in 0 until length) {
            array[i] = readFloat()
        }
        return array
    }


    fun readDouble(): Double {
        return Double.fromBits(readLong())
    }

    fun readDoubleArray(length: Int = readVarInt()): DoubleArray {
        check(length <= bytes.size / Double.SIZE_BYTES) { "Trying to allocate too much memory!" }
        val array = DoubleArray(length)
        for (i in 0 until length) {
            array[i] = readDouble()
        }
        return array
    }


    fun readFixedPointNumberInt(): Double {
        return readInt() / 32.0
    }

    fun readFixedPointNumberByte(): Double {
        return readByte() / 32.0
    }

    fun readBoolean(): Boolean {
        return readUnsignedByte() == 1
    }

    @JvmOverloads
    fun readString(length: Int = readVarInt()): String {
        val string = String(readByteArray(length), StandardCharsets.UTF_8)
        check(string.length <= 32767) { "String max string length exceeded ${string.length} > ${32767}" }
        return string
    }

    @JvmOverloads
    fun readNullString(length: Int = readVarInt()): String? {
        val string = readString(length)
        if (string.isBlank()) {
            return null
        }
        return string
    }

    fun readRest(): ByteArray {
        return readByteArray(length = size - pointer)
    }

    fun readUnsignedShortsLE(length: Int): IntArray {
        require(length <= size) { "Trying to allocate to much memory" }
        val ret = IntArray(length)
        for (i in 0 until length) {
            ret[i] = readUnsignedByte() or (readUnsignedByte() shl 8)
        }
        return ret
    }

    fun <T> readOptional(reader: InByteBuffer.() -> T): T? {
        return if (readBoolean()) {
            reader(this)
        } else {
            null
        }
    }

}