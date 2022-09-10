package us.isebas.compass.client.protocol.packet.clientbound

import us.isebas.compass.client.WrappedBuff
import us.isebas.compass.client.protocol.handler.PacketHandler

class ClientboundDisconnectPacket : ClientboundPacket {
    override fun decode(buff: WrappedBuff) {
        // Nothing needs to be done
    }

    override fun handle(handler: PacketHandler) {
        handler.handleClientboundDisconnect(this)
    }
}