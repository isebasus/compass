package us.isebas.compass.client.pipeline

import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageEncoder
import us.isebas.compass.client.connection.Connection
import us.isebas.compass.client.util.io.OutByteBuffer
import us.isebas.compass.client.protocol.Protocol
import us.isebas.compass.client.protocol.packet.serverbound.ServerboundPacket


class PacketEncoder(private val connection: Connection) : MessageToMessageEncoder<ServerboundPacket>() {

    companion object {
        const val NAME = "packet_encoder"
    }

    override fun encode(ctx: ChannelHandlerContext?, packet: ServerboundPacket, out: MutableList<Any>) {
        val packetId = Protocol.packetId(packet)

        check(packetId != -1) { "Packet " + packet.javaClass + " is not registered." }

        val data = OutByteBuffer()
        data.writeVarInt(packetId)
        packet.encode(data)

        out += data.toArray()
    }
}