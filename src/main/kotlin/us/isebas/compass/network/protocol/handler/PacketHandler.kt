package us.isebas.compass.network.protocol.handler

import us.isebas.compass.network.protocol.packet.serverbound.ServerboundHandshakePacket
import us.isebas.compass.network.protocol.packet.serverbound.ServerboundPingPacket
import us.isebas.compass.network.protocol.packet.serverbound.ServerboundStatusPacket

interface PacketHandler {

    fun handleHandshake()

    fun handleStatus()

    fun handlePing(packet: ServerboundPingPacket)
}