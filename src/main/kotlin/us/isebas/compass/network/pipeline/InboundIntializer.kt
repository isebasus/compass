package us.isebas.compass.network.pipeline

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import us.isebas.compass.document.MinecraftServer
import us.isebas.compass.network.Connection
import us.isebas.compass.network.protocol.ConnectionState

open class InboundIntializer(private val minecraftServer: MinecraftServer) : ChannelInitializer<SocketChannel>() {

    private var connection: Connection? = null

    open fun getConnection(): Connection? {
        return connection
    }

    override fun initChannel(channel: SocketChannel) {
        connection = Connection(minecraftServer, channel)
        channel.pipeline()
                .addLast(PacketDecoder(connection!!))
                .addLast(PacketEncoder(connection!!))
                .addLast(InboundHandler(connection!!))
    }
}