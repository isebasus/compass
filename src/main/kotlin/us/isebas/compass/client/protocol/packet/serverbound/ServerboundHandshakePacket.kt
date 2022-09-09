package us.isebas.compass.client.protocol.packet.serverbound

import us.isebas.compass.document.MinecraftServer
import us.isebas.compass.client.WrappedBuff


class ServerboundHandshakePacket(
        private val server: MinecraftServer,
        private val nextState: Int)
    : ServerboundPacket {

    override fun encode(buff: WrappedBuff) {
        buff.writeInt(server.protocolVersion)
                .writeString(server.address)
                .writeShort(server.port.toShort())
                .writeInt(nextState)
    }

}