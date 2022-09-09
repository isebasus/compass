package us.isebas.compass.network.protocol.handler

import net.kyori.adventure.text.Component
import us.isebas.compass.network.protocol.packet.clientbound.ClientboundDisconnectPacket
import us.isebas.compass.network.protocol.packet.clientbound.ClientboundPingPacket
import us.isebas.compass.network.protocol.packet.clientbound.ClientboundStatusPacket
import us.isebas.compass.network.protocol.packet.serverbound.ServerboundPingPacket

interface PacketHandler {

    fun handleHandshake(ping: Boolean)

    fun handleStatus(packet: ClientboundStatusPacket)

    fun handlePing(packet: ClientboundPingPacket)

    fun handleC2SDisconnect(reason: Component)

    fun handleS2CDisconnect(packet: ClientboundDisconnectPacket)
}