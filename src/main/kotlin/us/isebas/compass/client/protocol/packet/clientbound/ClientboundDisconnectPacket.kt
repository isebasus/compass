package us.isebas.compass.client.protocol.packet.clientbound

import us.isebas.compass.client.util.io.InByteBuffer
import us.isebas.compass.client.protocol.handler.PacketHandler

class ClientboundDisconnectPacket : ClientboundPacket {
    override fun decode(buff: InByteBuffer) {
        // Nothing needs to be done
    }

    override fun handle(handler: PacketHandler) {
        handler.handleClientboundDisconnect(this)
    }
}