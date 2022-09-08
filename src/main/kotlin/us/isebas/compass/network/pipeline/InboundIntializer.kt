package us.isebas.compass.network.pipeline

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import us.isebas.compass.document.MinecraftServer

class InboundIntializer(val minecraftServer: MinecraftServer) : ChannelInitializer<SocketChannel>() {
    override fun initChannel(channel: SocketChannel) {
        val connection: Connection
    }
}