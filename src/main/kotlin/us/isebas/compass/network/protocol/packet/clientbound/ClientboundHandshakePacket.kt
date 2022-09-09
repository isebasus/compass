package us.isebas.compass.network.protocol.packet.clientbound

import us.isebas.compass.network.WrappedBuff
import us.isebas.compass.network.protocol.handler.PacketHandler

open class ClientboundHandshakePacket : ClientboundPacket {
    private var data: String? = null

    open fun data(): String? {
        return data
    }

    override fun decode(buff: WrappedBuff){
        data = buff.readString()
    }

    override fun handle(handler: PacketHandler) {
        println("Received handshake packet")
    }
}