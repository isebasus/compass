package us.isebas.compass.network.pipeline

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import us.isebas.compass.network.Connection
import us.isebas.compass.network.WrappedBuff
import us.isebas.compass.network.protocol.Protocol
import us.isebas.compass.network.protocol.packet.serverbound.ServerboundPacket


class PacketEncoder(connection: Connection) : MessageToByteEncoder<ServerboundPacket?>() {
    private val connection: Connection


    override fun encode(context: ChannelHandlerContext?, packet: ServerboundPacket?, buff: ByteBuf?) {
        val packetId: Int? = packet?.let { Protocol.packetId(it) }

        if (packet == null || packetId == null || buff == null) {
            // TODO idk handle something
            return
        }
        check(packetId != -1) { "Packet " + packet.javaClass + " is not registered." }

        buff.writeByte(packetId)
        packet.encode(WrappedBuff.wrap(buff))
    }

    init {
        this.connection = connection
    }
}