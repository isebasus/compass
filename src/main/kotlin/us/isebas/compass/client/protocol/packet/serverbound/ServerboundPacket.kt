package us.isebas.compass.client.protocol.packet.serverbound

import us.isebas.compass.client.util.io.OutByteBuffer
import us.isebas.compass.client.protocol.packet.Packet

interface ServerboundPacket : Packet {

    fun encode(buff: OutByteBuffer)
}