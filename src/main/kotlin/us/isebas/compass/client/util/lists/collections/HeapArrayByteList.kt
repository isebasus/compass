package us.isebas.compass.client.util.lists.collections

class HeapArrayByteList(
    initialSize: Int = DEFAULT_INITIAL_SIZE,
) : AbstractByteList() {
    private var data: ByteArray = ByteArray(initialSize)
    override val limit: Int
        get() = data.size
    override var size = 0
    override val isEmpty: Boolean
        get() = size == 0

    private val nextGrowStep = when {
        initialSize <= 0 -> DEFAULT_INITIAL_SIZE
        initialSize <= 50 -> 50
        else -> initialSize
    }

    private var output: ByteArray = ByteArray(0)
    private var outputUpToDate = false

    private fun checkFinalized() {
        if (finished) {
            throw IllegalStateException("ByteArrayList is already finalized!")
        }
    }

    override fun clear() {
        checkFinalized()
        size = 0
        outputUpToDate = false
        output = ByteArray(0)
    }

    override fun ensureSize(needed: Int) {
        checkFinalized()
        if (limit - size >= needed) {
            return
        }
        var newSize = data.size
        while (newSize - size < needed) {
            newSize += nextGrowStep
        }
        val oldData = data
        data = ByteArray(newSize)
        System.arraycopy(oldData, 0, data, 0, oldData.size)
    }

    override fun add(value: Byte) {
        ensureSize(1)
        data[size++] = value
        outputUpToDate = false
    }

    override fun addAll(bytes: ByteArray) {
        ensureSize(bytes.size)
        System.arraycopy(bytes, 0, data, size, bytes.size)
        size += bytes.size
        outputUpToDate = false
    }

    override fun addAll(byteList: AbstractByteList) {
        ensureSize(byteList.size)
        val source: ByteArray = if (byteList is HeapArrayByteList) {
            if (byteList.finished) {
                byteList.output
            } else {
                byteList.data
            }
        } else {
            byteList.toArray()
        }
        System.arraycopy(source, 0, data, size, byteList.size)
        size += byteList.size
    }

    private fun checkOutputArray() {
        if (outputUpToDate) {
            return
        }
        output = ByteArray(size)
        System.arraycopy(data, 0, output, 0, size)
        outputUpToDate = true
    }

    override fun toArray(): ByteArray {
        checkOutputArray()
        return output
    }

    override fun finish() {
        finished = true
        checkOutputArray()
        data = ByteArray(0)
    }


    private companion object {
        private const val DEFAULT_INITIAL_SIZE = 1000
    }
}
