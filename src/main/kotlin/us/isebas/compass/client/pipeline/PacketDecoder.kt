package us.isebas.compass.client.pipeline

import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageDecoder
import us.isebas.compass.client.connection.Connection
import us.isebas.compass.client.FlowDirection
import us.isebas.compass.client.protocol.packet.clientbound.ClientboundPacket
import us.isebas.compass.client.util.io.InByteBuffer

class PacketDecoder(private val connection: Connection) : MessageToMessageDecoder<ByteArray>() {
    companion object {
        const val NAME = "packet_decoder"
    }

    override fun decode(ctx: ChannelHandlerContext, arr: ByteArray, out: MutableList<Any>) {
        val buffer = InByteBuffer(arr)
        val packetId = buffer.readVarInt()

        val packet = connection.state().packetById(FlowDirection.CLIENTBOUND, packetId) as ClientboundPacket? ?: return
        packet.decode(buffer)

        out.add(packet)
    }

}