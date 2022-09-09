package us.isebas.compass.network.pipeline

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import us.isebas.compass.network.Connection
import us.isebas.compass.network.protocol.packet.clientbound.ClientboundPacket
import us.isebas.compass.network.protocol.packet.serverbound.ServerboundPacket

class InboundHandler(private val connection: Connection) : SimpleChannelInboundHandler<ClientboundPacket>() {

    override fun channelRead0(context: ChannelHandlerContext?, packet: ClientboundPacket?) {
        if (packet == null || context == null) {
            return
        }
        println("Received packet ${packet.javaClass.simpleName}.")
        packet.handle(connection.packetHandler())
    }
}