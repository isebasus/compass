package us.isebas.compass.network.pipeline

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ReplayingDecoder
import us.isebas.compass.network.Connection
import us.isebas.compass.network.FlowDirection
import us.isebas.compass.network.WrappedBuff
import us.isebas.compass.network.protocol.packet.clientbound.ClientboundPacket
import us.isebas.compass.network.protocol.packet.serverbound.ServerboundPacket
import java.nio.ByteBuffer.wrap


class PacketDecoder(private val connection: Connection) : ReplayingDecoder<ClientboundPacket>() {

    override fun decode(context: ChannelHandlerContext?, buff: ByteBuf, decoded: MutableList<Any>?) {
        val packetId = buff.readUnsignedByte()
        val packet = connection.state().packetById(FlowDirection.CLIENTBOUND, packetId.toInt()) as ClientboundPacket? ?: return

        packet.decode(WrappedBuff.wrap(buff))
        decoded!!.add(packet)
    }

}