package us.isebas.compass.network.protocol.packet.clientbound

import us.isebas.compass.network.WrappedBuff
import us.isebas.compass.network.protocol.handler.PacketHandler
import us.isebas.compass.network.protocol.packet.Packet

interface ClientboundPacket : Packet {

    fun decode(buff: WrappedBuff)

    fun handle(handler: PacketHandler)
}