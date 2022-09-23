package us.isebas.compass.client.protocol.packet.serverbound

import us.isebas.compass.client.util.io.OutByteBuffer

class ServerboundPingPacket : ServerboundPacket {
    override fun encode(buff: OutByteBuffer) {
        buff.writeLong(0x1)
    }
}