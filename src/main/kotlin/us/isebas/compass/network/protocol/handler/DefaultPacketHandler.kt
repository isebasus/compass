package us.isebas.compass.network.protocol.handler

import com.beust.klaxon.Klaxon
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import us.isebas.compass.document.MinecraftServer
import us.isebas.compass.document.ServerStatus
import us.isebas.compass.network.Connection
import us.isebas.compass.network.protocol.ConnectionState
import us.isebas.compass.network.protocol.packet.clientbound.ClientboundDisconnectPacket
import us.isebas.compass.network.protocol.packet.clientbound.ClientboundStatusPacket
import us.isebas.compass.network.protocol.packet.serverbound.ServerboundDisconnectPacket
import us.isebas.compass.network.protocol.packet.serverbound.ServerboundHandshakePacket
import us.isebas.compass.network.protocol.packet.serverbound.ServerboundStatusPacket
import us.isebas.compass.serializable.status.StatusResponse
import javax.naming.InvalidNameException

class DefaultPacketHandler(private val server: MinecraftServer, private val connection: Connection) : PacketHandler {
    /* Serverbound packets */

    override fun handleHandshake() {
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
            handleServerboundDisconnect(text("Already handled status"))
            return
        }
        // Get data from packet
        val data: String? = packet.data()
        if (data == null) {
            server.status = ServerStatus.OFFLINE
            handleServerboundDisconnect(text("Invalid data."))
            throw InvalidNameException("No data received from server ${server.address}")
        }

        // Deserialize data
        val deserializedData = Klaxon().parse<StatusResponse>(data)
        server.serverVersion = deserializedData?.version?.name.toString()
        server.maxPlayerCount = deserializedData?.players?.max
        server.playerCount = deserializedData?.players?.online
        server.description = deserializedData?.description?.text.toString()
        server.favicon = deserializedData?.favicon.toString()
        server.status = ServerStatus.ONLINE

        handleServerboundDisconnect(text("Completed Tasks"))
    }

    override fun handleClientboundDisconnect(packet: ClientboundDisconnectPacket) {
        connection.close()
    }
}