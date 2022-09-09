package us.isebas.compass.network.pipeline

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import us.isebas.compass.document.MinecraftServer
import us.isebas.compass.network.Connection
import us.isebas.compass.network.protocol.ConnectionState

class InboundIntializer(private val minecraftServer: MinecraftServer) : ChannelInitializer<SocketChannel>() {

    override fun initChannel(channel: SocketChannel) {
        val connection: Connection = Connection(minecraftServer, channel)
        channel.pipeline()
                .addLast(PacketDecoder(connection))
                .addLast(PacketEncoder(connection))
                .addLast(InboundHandler(connection))
    }
}