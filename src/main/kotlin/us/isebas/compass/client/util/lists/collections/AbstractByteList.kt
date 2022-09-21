package us.isebas.compass.client.util.lists.collections

import us.isebas.compass.client.util.lists.AbstractPrimitiveList

abstract class AbstractByteList : AbstractPrimitiveList<Byte>() {

    abstract fun addAll(bytes: ByteArray)
    abstract fun addAll(byteList: AbstractByteList)

    abstract fun toArray(): ByteArray
}
