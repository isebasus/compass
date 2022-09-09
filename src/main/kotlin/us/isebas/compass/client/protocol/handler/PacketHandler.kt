package us.isebas.compass.client.protocol.handler

import net.kyori.adventure.text.Component
import us.isebas.compass.client.protocol.packet.clientbound.ClientboundDisconnectPacket
import us.isebas.compass.client.protocol.packet.clientbound.ClientboundStatusPacket

interface PacketHandler {

    /* Serverbound packets */

    fun handleHandshake()

    fun handleServerboundDisconnect(reason: Component)

    /* Clientbound packets */

    fun handleStatus(packet: ClientboundStatusPacket)

    fun handleClientboundDisconnect(packet: ClientboundDisconnectPacket)
}