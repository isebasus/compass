package us.isebas.compass.client.pipeline

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ReplayingDecoder
import us.isebas.compass.client.Connection
import us.isebas.compass.client.FlowDirection
import us.isebas.compass.client.WrappedBuff
import us.isebas.compass.client.protocol.packet.clientbound.ClientboundPacket

class PacketDecoder(private val connection: Connection) : ReplayingDecoder<ClientboundPacket>() {

    override fun decode(context: ChannelHandlerContext?, buff: ByteBuf, decoded: MutableList<Any>?) {
        val packetId = buff.readUnsignedByte()
        val packet = connection.state().packetById(FlowDirection.CLIENTBOUND, packetId.toInt()) as ClientboundPacket? ?: return

        packet.decode(WrappedBuff.wrap(buff))
        decoded!!.add(packet)
    }

}