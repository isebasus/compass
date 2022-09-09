package us.isebas.compass.client.protocol.packet.serverbound

import us.isebas.compass.client.WrappedBuff
import us.isebas.compass.client.protocol.packet.Packet

interface ServerboundPacket : Packet {

    fun encode(buff: WrappedBuff)
}