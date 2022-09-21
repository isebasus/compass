package us.isebas.compass.client.util.threading


import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

class NamedThreadFactory(
    val format: String,
) : ThreadFactory {
    var count = AtomicInteger(0)

    override fun newThread(runnable: Runnable): Thread {
        return Thread(runnable, format.format(count.incrementAndGet()))
    }
}
