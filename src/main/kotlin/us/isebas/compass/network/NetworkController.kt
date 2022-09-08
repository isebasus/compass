package us.isebas.compass.network

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.ServerChannel
import io.netty.channel.epoll.Epoll
import io.netty.channel.epoll.EpollEventLoopGroup
import io.netty.channel.epoll.EpollServerSocketChannel
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import us.isebas.compass.document.MinecraftServer

class NetworkController(val server: MinecraftServer){

    open fun start() {
        val bootstrap = createServerBootstrap()
        val channelFuture = server.port?.let { bootstrap.bind(it).sync() }
        if (channelFuture == null) {
            System.err.println("Unable to start server: ${server.address}")
            return
        }
        if (!channelFuture.isSuccess()) {
            System.err.println("Unable to bind server: ${server.address} to port: ${server.port}")
            return
        }
        channelFuture.channel().closeFuture().sync()
    }

    private fun createServerBootstrap(): ServerBootstrap {
        val serverBootstrap = ServerBootstrap()
        serverBootstrap.group(createGroup())
        serverBootstrap.channel(channelClass())
        serverBootstrap.childOption(ChannelOption.TCP_NODELAY, true)
        serverBootstrap.childHandler(InboundInitializer(server))
        return serverBootstrap
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