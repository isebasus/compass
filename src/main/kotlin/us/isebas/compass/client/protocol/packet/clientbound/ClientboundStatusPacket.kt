package us.isebas.compass.client.protocol.packet.clientbound

import us.isebas.compass.client.util.io.InByteBuffer
import us.isebas.compass.client.protocol.handler.PacketHandler

class ClientboundStatusPacket : ClientboundPacket {
    private var data: String? = null

    fun data(): String? {
        return data
    }

    override fun decode(buff: InByteBuffer) {
        data = buff.readString()
    }

    override fun handle(handler: PacketHandler) {
        handler.handleStatus(this)
    }
}