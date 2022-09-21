package us.isebas.compass.client.util.lists

abstract class AbstractPrimitiveList<T> : Clearable {
    var finished: Boolean = false
        protected set
    abstract val limit: Int
    abstract val size: Int
    abstract val isEmpty: Boolean

    abstract fun ensureSize(needed: Int)
    abstract fun add(value: T)

    abstract fun finish()

}