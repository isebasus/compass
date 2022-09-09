package us.isebas.compass.network.protocol.packet.serverbound

import us.isebas.compass.document.MinecraftServer
import us.isebas.compass.network.WrappedBuff
import us.isebas.compass.network.protocol.handler.PacketHandler


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