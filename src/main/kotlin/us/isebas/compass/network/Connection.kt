package us.isebas.compass.network

import io.netty.channel.Channel
import net.kyori.adventure.text.Component;
import us.isebas.compass.document.MinecraftServer
import us.isebas.compass.network.protocol.ConnectionState
import us.isebas.compass.network.protocol.handler.DefaultPacketHandler
import us.isebas.compass.network.protocol.handler.PacketHandler
import us.isebas.compass.network.protocol.packet.Packet


class Connection(private val server: MinecraftServer, private val channel: Channel) {
    private val packetHandler: PacketHandler
    private var state: ConnectionState

    init {
        // TODO implement ping handling
        packetHandler = DefaultPacketHandler(server, this, false)
        state = ConnectionState.HANDSHAKING
        packetHandler.handleHandshake()
    }

    fun state(): ConnectionState {
        return state
    }

    fun packetHandler(): PacketHandler {
        return packetHandler
    }

    fun setState(state: ConnectionState) {
        this.state = state
    }

    fun sendPacket(packet: Packet?) {
        channel.writeAndFlush(packet)
    }

    fun close() {
        channel.closeFuture()
    }
}