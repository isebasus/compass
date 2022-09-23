package us.isebas.compass.client.protocol.packet.clientbound

import us.isebas.compass.client.protocol.handler.PacketHandler
import us.isebas.compass.client.util.io.InByteBuffer
import kotlin.properties.Delegates

class ClientboundPingPacket : ClientboundPacket {
    private var pingPayload: Long? = null

    fun data(): Long? {
        return pingPayload
    }

    override fun decode(buff: InByteBuffer) {
        pingPayload = buff.readLong()
    }

    override fun handle(handler: PacketHandler) {
        handler.handleClientboundPing(this)
    }
}