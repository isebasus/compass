package us.isebas.compass.client.protocol.packet.serverbound

import us.isebas.compass.client.util.io.OutByteBuffer
import us.isebas.compass.document.MinecraftServer


class ServerboundHandshakePacket(
        private val server: MinecraftServer,
        private val nextState: Int)
    : ServerboundPacket {

    override fun encode(buff: OutByteBuffer) {
        buff.writeVarInt(server.protocolVersion)
        buff.writeString(server.address)
        buff.writeShort(server.port.toShort())
        buff.writeVarInt(nextState)
    }

}