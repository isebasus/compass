package us.isebas.compass.client.pipeline

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import us.isebas.compass.client.Connection
import us.isebas.compass.client.protocol.packet.clientbound.ClientboundPacket

class InboundHandler(private val connection: Connection) : SimpleChannelInboundHandler<ClientboundPacket>() {

    companion object {
        const val NAME = "client_packet_handler"
    }

    override fun channelRead0(context: ChannelHandlerContext, packet: ClientboundPacket) {
        println("Client: Received packet ${packet.javaClass.simpleName}.")
        packet.handle(connection.packetHandler())
    }
}