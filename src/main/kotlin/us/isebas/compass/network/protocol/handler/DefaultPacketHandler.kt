package us.isebas.compass.network.protocol.handler

import net.kyori.adventure.text.Component.text
import us.isebas.compass.document.MinecraftServer
import us.isebas.compass.network.Connection
import us.isebas.compass.network.protocol.ConnectionState
import us.isebas.compass.network.protocol.packet.clientbound.ClientboundHandshakePacket
import us.isebas.compass.network.protocol.packet.serverbound.ServerboundHandshakePacket
import us.isebas.compass.network.protocol.packet.serverbound.ServerboundPingPacket
import us.isebas.compass.network.protocol.packet.serverbound.ServerboundStatusPacket

class DefaultPacketHandler(private val server: MinecraftServer, private val connection: Connection) : PacketHandler {
    override fun handleHandshake() {
        if (connection.state() != ConnectionState.HANDSHAKING) {
            connection.disconnect(text("Already completed handshaking phase."))
            return
        }
        connection.sendPacket(ServerboundHandshakePacket(server, 1))
        connection.setState(ConnectionState.STATUS)
        connection.sendPacket(ServerboundStatusPacket())
    }

    override fun handleStatus() {
        connection.sendPacket(ServerboundStatusPacket())
    }

    override fun handlePing(packet: ServerboundPingPacket) {
        TODO("Not yet implemented")
    }

}