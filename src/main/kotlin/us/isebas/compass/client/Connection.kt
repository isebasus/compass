package us.isebas.compass.client

import io.netty.channel.Channel
import us.isebas.compass.document.MinecraftServer
import us.isebas.compass.client.protocol.ConnectionState
import us.isebas.compass.client.protocol.handler.DefaultPacketHandler
import us.isebas.compass.client.protocol.handler.PacketHandler
import us.isebas.compass.client.protocol.packet.Packet


class Connection(private val server: MinecraftServer, private val channel: Channel) {
    private val packetHandler: PacketHandler
    private var state: ConnectionState

    init {
        packetHandler = DefaultPacketHandler(server, this)
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