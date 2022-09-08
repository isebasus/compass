package us.isebas.compass.network.protocol.handler

import us.isebas.compass.network.protocol.packet.serverbound.ServerboundHandshakePacket
import us.isebas.compass.network.protocol.packet.serverbound.ServerboundPingPacket
import us.isebas.compass.network.protocol.packet.serverbound.ServerboundStatusPacket

interface PacketHandler {

    fun handleHandshake(packet: ServerboundHandshakePacket)

    fun handleStatus(packet: ServerboundStatusPacket)

    fun handlePing(packet: ServerboundPingPacket)
}