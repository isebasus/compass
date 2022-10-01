package us.isebas.compass.client.protocol.handler

import com.beust.klaxon.Klaxon
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import us.isebas.compass.document.MinecraftServer
import us.isebas.compass.client.connection.Connection
import us.isebas.compass.client.protocol.ConnectionState
import us.isebas.compass.client.protocol.packet.clientbound.ClientboundDisconnectPacket
import us.isebas.compass.client.protocol.packet.clientbound.ClientboundPingPacket
import us.isebas.compass.client.protocol.packet.clientbound.ClientboundStatusPacket
import us.isebas.compass.client.protocol.packet.serverbound.ServerboundDisconnectPacket
import us.isebas.compass.client.protocol.packet.serverbound.ServerboundHandshakePacket
import us.isebas.compass.client.protocol.packet.serverbound.ServerboundPingPacket
import us.isebas.compass.client.protocol.packet.serverbound.ServerboundStatusPacket
import us.isebas.compass.document.ServerError
import us.isebas.compass.serializable.status.StatusResponse
import java.util.concurrent.CompletableFuture
import javax.naming.InvalidNameException

class DefaultPacketHandler(private val server: MinecraftServer,
                           private val connection: Connection,
                           private var completableFuture: CompletableFuture<Void>
                                ) : PacketHandler {

    private var systemTime: Long = 0

    /* Serverbound packets */
    override fun handleHandshake() {
        if (connection.state() != ConnectionState.HANDSHAKING) {
            handleServerboundDisconnect(text("Already handled handshake"))
            return
        }
        connection.sendPacket(ServerboundHandshakePacket(server, 1))

        // Send status packet
        connection.setState(ConnectionState.STATUS)
        handleServerboundStatusPing()
    }

    override fun handleServerboundStatusPing() {
        if (connection.state() != ConnectionState.STATUS) {
            handleServerboundDisconnect(text("Connection state is not STATUS"))
        }

        connection.sendPacket(ServerboundStatusPacket())

        server.numberOfPings++
        systemTime = System.currentTimeMillis()
    }

    override fun handleServerboundPing() {
        if (connection.state() != ConnectionState.STATUS) {
            handleServerboundDisconnect(text("Connection state is not STATUS"))
        }

        connection.sendPacket(ServerboundPingPacket())
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
        val data = packet.data()
        if (data == null) {
            server.status = ServerError.BADREQUEST
            handleServerboundDisconnect(text("Invalid data."))
            throw InvalidNameException("No data received from server ${server.hostname}")
        }

        // Deserialize data
        val deserializedData = Klaxon().parse<StatusResponse>(data)
        server.protocolVersion = deserializedData?.version?.protocol ?: 0
        server.serverVersion = deserializedData?.version?.name.toString()
        server.maxPlayerCount = deserializedData?.players?.max
        deserializedData?.players?.online?.let { server.playerCount.add(it) }
        server.description = deserializedData?.description?.text.toString()
        server.favicon = deserializedData?.favicon.toString()
        server.status = ServerError.SUCCESS

        handleServerboundPing()
    }

    override fun handleClientboundPing(packet: ClientboundPingPacket) {
        val pingTime = System.currentTimeMillis() - systemTime

        if (connection.state() != ConnectionState.STATUS) {
            handleServerboundDisconnect(text("Already handled ping"))
            return
        }
        server.ping = pingTime

        var average: Long = server.averagePing.last()
        average += ((pingTime - average) / server.numberOfPings)
        server.averagePing.add(average);

        completableFuture.complete(null)
        handleServerboundDisconnect(text("Client: Completed fetching server status and ping"))
    }

    override fun handleClientboundDisconnect(packet: ClientboundDisconnectPacket) {
        connection.close()
    }
}