package us.isebas.compass.client.protocol.packet.clientbound

import us.isebas.compass.client.WrappedBuff
import us.isebas.compass.client.protocol.handler.PacketHandler
import us.isebas.compass.client.protocol.packet.Packet

interface ClientboundPacket : Packet {

    fun decode(buff: WrappedBuff)

    fun handle(handler: PacketHandler)
}