package us.isebas.compass.client

import io.netty.bootstrap.Bootstrap
import io.netty.channel.EventLoopGroup
import io.netty.channel.ServerChannel
import io.netty.channel.epoll.Epoll
import io.netty.channel.epoll.EpollEventLoopGroup
import io.netty.channel.epoll.EpollServerSocketChannel
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.DecoderException
import io.netty.handler.codec.EncoderException
import us.isebas.compass.document.MinecraftServer
import us.isebas.compass.document.ServerStatus
import us.isebas.compass.client.pipeline.InboundIntializer

open class ClientController(private val server: MinecraftServer){
    private var inboundInitializer: InboundIntializer? = null

    open fun start() {
        val bootstrap = createClientBootstrap()
        val future = bootstrap.connect(server.address, server.port)
        future.addListener {
            if (!it.isSuccess) {
                handleError(it.cause())
            }
        }
    }

    open fun disconnect() {
        inboundInitializer?.getConnection()?.close()
        return
    }

    open fun handleError(error: Throwable) {
        var cause = error
        if (cause is DecoderException) {
            cause = error.cause ?: cause
        } else if (cause is EncoderException) {
            cause = error.cause ?: cause
        }
        error.printStackTrace()
        server.status = ServerStatus.OFFLINE
        disconnect()
    }

    private fun createClientBootstrap(): Bootstrap {
        val clientBootstrap = Bootstrap()
        inboundInitializer = InboundIntializer(server)

        // Initialize bootstrap
        clientBootstrap.group(createGroup())
        clientBootstrap.channel(channelClass())
        clientBootstrap.handler(inboundInitializer)
        return clientBootstrap
    }

    private fun createGroup(): EventLoopGroup {
        return if (Epoll.isAvailable())
            EpollEventLoopGroup()
            else NioEventLoopGroup()
    }

    private fun channelClass(): Class<out ServerChannel> {
        return if (Epoll.isAvailable())
            EpollServerSocketChannel::class.java
            else NioServerSocketChannel::class.java
    }
}