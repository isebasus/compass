package us.isebas.compass.network.protocol.packet.serverbound

import us.isebas.compass.network.WrappedBuff
import us.isebas.compass.network.protocol.handler.PacketHandler
import us.isebas.compass.network.protocol.packet.Packet

interface ServerboundPacket : Packet {

    fun encode(buff: WrappedBuff)
}