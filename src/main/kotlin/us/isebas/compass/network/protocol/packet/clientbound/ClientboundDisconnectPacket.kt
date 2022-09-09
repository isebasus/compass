package us.isebas.compass.network.protocol.packet.clientbound

import us.isebas.compass.network.WrappedBuff
import us.isebas.compass.network.protocol.handler.PacketHandler

class ClientboundDisconnectPacket : ClientboundPacket {
    override fun decode(buff: WrappedBuff) {
        // Nothing needs to be done
    }

    override fun handle(handler: PacketHandler) {
        handler.handleDisconnect(this)
    }
}