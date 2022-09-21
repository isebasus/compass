package us.isebas.compass.client.pipeline

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.timeout.ReadTimeoutHandler
import us.isebas.compass.client.Connection
import us.isebas.compass.document.MinecraftServer

open class InboundIntializer(private val minecraftServer: MinecraftServer) : ChannelInitializer<SocketChannel>() {

    private lateinit var connection: Connection
    private val SOCKET_TIMEOUT = 30000
    private val maxLength: Int = 1 shl 16

    open fun getConnection(): Connection? {
        return connection
    }

    override fun initChannel(channel: SocketChannel) {
        connection = Connection(minecraftServer, channel)
        val pipeline = channel.pipeline()

        pipeline.addLast("timeout", ReadTimeoutHandler(SOCKET_TIMEOUT / 1000))

        pipeline.addLast(LengthDecoder.NAME, LengthDecoder(maxLength))
        pipeline.addLast(PacketDecoder.NAME, PacketDecoder(connection))
        pipeline.addLast(InboundHandler.NAME, InboundHandler(connection))

        pipeline.addLast(LengthEncoder.NAME, LengthEncoder(maxLength))
        pipeline.addLast(PacketEncoder.NAME, PacketEncoder(connection))

        connection.packetHandler().handleHandshake()
    }

    override fun channelActive(context: ChannelHandlerContext) {
        super.channelActive(context)
        println("Channel active: $context")
    }

    override fun exceptionCaught(channel: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        channel.close()
    }
}