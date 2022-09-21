package us.isebas.compass.client.protocol.packet.clientbound

import us.isebas.compass.client.util.io.InByteBuffer
import us.isebas.compass.client.protocol.handler.PacketHandler
import us.isebas.compass.client.protocol.packet.Packet

interface ClientboundPacket : Packet {

    fun decode(buff: InByteBuffer)

    fun handle(handler: PacketHandler)
}