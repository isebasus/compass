package us.isebas.compass.network.protocol.handler

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import us.isebas.compass.document.MinecraftServer
import us.isebas.compass.document.ServerStatus
import us.isebas.compass.network.Connection
import us.isebas.compass.network.protocol.ConnectionState
import us.isebas.compass.network.protocol.packet.clientbound.ClientboundDisconnectPacket
import us.isebas.compass.network.protocol.packet.clientbound.ClientboundPingPacket
import us.isebas.compass.network.protocol.packet.clientbound.ClientboundStatusPacket
import us.isebas.compass.network.protocol.packet.serverbound.ServerboundDisconnectPacket
import us.isebas.compass.network.protocol.packet.serverbound.ServerboundHandshakePacket
import us.isebas.compass.network.protocol.packet.serverbound.ServerboundStatusPacket

class DefaultPacketHandler(private val server: MinecraftServer, private val connection: Connection) : PacketHandler {
    /* Serverbound packets */

    override fun handleHandshake(ping: Boolean) {
        if (connection.state() != ConnectionState.HANDSHAKING) {
            handleServerboundDisconnect(text("Already handled handshake"))
            return
        }
        connection.sendPacket(ServerboundHandshakePacket(server, 1))

        // Send status packet
        connection.setState(ConnectionState.STATUS)
        connection.sendPacket(ServerboundStatusPacket())
    }

    override fun handleServerboundDisconnect(reason: Component) {
        connection.sendPacket(ServerboundDisconnectPacket(reason))
    }

    /* Clientbound packets */

    override fun handleStatus(packet: ClientboundStatusPacket) {
        if (connection.state() != ConnectionState.STATUS) {
            // Disconnect or something
            handleServerboundDisconnect(text("Already handled status"))
            return
        }
        val data: String? = packet.data()
        // TODO deserialize data
        server.status = ServerStatus.ONLINE
    }

    override fun handlePing(packet: ClientboundPingPacket) {
        TODO("Not yet implemented")
    }

    override fun handleClientboundDisconnect(packet: ClientboundDisconnectPacket) {
        connection.close()
    }
}