package us.isebas.compass.client.protocol.packet.clientbound

import us.isebas.compass.client.WrappedBuff
import us.isebas.compass.client.protocol.handler.PacketHandler

class ClientboundStatusPacket : ClientboundPacket {
    private var data: String? = null

    open fun data(): String? {
        return data
    }

    override fun decode(buff: WrappedBuff) {
        data = buff.readString()
    }

    override fun handle(handler: PacketHandler) {
        handler.handleStatus(this)
    }
}