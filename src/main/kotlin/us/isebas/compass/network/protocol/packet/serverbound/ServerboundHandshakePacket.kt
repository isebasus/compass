package us.isebas.compass.network.protocol.packet.serverbound

import us.isebas.compass.network.WrappedBuff
import us.isebas.compass.network.protocol.handler.PacketHandler


abstract class ServerboundHandshakePacket : ServerboundPacket {
    private var username: String? = null

    open fun username(): String? {
        return username
    }

    override fun decode(buff: WrappedBuff) {
        username = buff.readString()
    }

    override fun handle(handler: PacketHandler) {
        handler.handleHandshake(this)
    }
}