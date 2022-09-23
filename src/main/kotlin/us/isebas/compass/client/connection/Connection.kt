package us.isebas.compass.client.connection

import io.netty.channel.Channel
import us.isebas.compass.document.MinecraftServer
import us.isebas.compass.client.protocol.ConnectionState
import us.isebas.compass.client.protocol.handler.DefaultPacketHandler
import us.isebas.compass.client.protocol.handler.PacketHandler
import us.isebas.compass.client.protocol.packet.Packet
import java.util.concurrent.CompletableFuture


class Connection(private val server: MinecraftServer,
                 private val channel: Channel,
                 private val completableFuture: CompletableFuture<Void>) {
    private val packetHandler: PacketHandler
    private var state: ConnectionState

    init {
        packetHandler = DefaultPacketHandler(server, this, completableFuture)
        state = ConnectionState.HANDSHAKING
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

    fun sendPacket(packet: Packet) {
        val lastWriteFuture = channel.writeAndFlush(packet)
        lastWriteFuture.addListener {
            if (!it.isSuccess) {
                println(it.cause().message)
            }
        }
    }

    fun close() {
        channel.closeFuture()
    }
}