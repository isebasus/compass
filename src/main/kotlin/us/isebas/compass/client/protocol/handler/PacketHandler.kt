package us.isebas.compass.client.protocol.handler

import net.kyori.adventure.text.Component
import us.isebas.compass.client.protocol.packet.clientbound.ClientboundDisconnectPacket
import us.isebas.compass.client.protocol.packet.clientbound.ClientboundPingPacket
import us.isebas.compass.client.protocol.packet.clientbound.ClientboundStatusPacket

interface PacketHandler {

    /* Serverbound packets */
    fun handleHandshake()

    fun handleServerboundStatusPing()

    fun handleServerboundPing()

    fun handleServerboundDisconnect(reason: Component)

    /* Clientbound packets */
    fun handleStatus(packet: ClientboundStatusPacket)

    fun handleClientboundPing(packet: ClientboundPingPacket)

    fun handleClientboundDisconnect(packet: ClientboundDisconnectPacket)
}