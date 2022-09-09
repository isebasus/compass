package us.isebas.compass.network.protocol

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.Object2IntMap
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import us.isebas.compass.network.FlowDirection
import us.isebas.compass.network.protocol.packet.Packet
import us.isebas.compass.network.protocol.packet.clientbound.ClientboundHandshakePacket
import us.isebas.compass.network.protocol.packet.serverbound.ServerboundHandshakePacket
import us.isebas.compass.network.protocol.packet.serverbound.ServerboundStatusPacket
import java.util.function.Function


object Protocol {
    private val PACKET_IDS: Object2IntMap<Class<out Packet?>> = Object2IntOpenHashMap()

    fun packetId(packet: Packet): Int {
        return PACKET_IDS.getInt(packet.javaClass)
    }

    private fun register(id: Int, clazz: Class<out Packet?>, direction: FlowDirection, vararg validStates: ConnectionState) {
        for (state in validStates) {
            state.packets().computeIfAbsent(direction) { Int2ObjectOpenHashMap() }[id] = clazz
        }
        PACKET_IDS[clazz] = id
    }

    init {
        register(0x00, ClientboundHandshakePacket::class.java, FlowDirection.CLIENTBOUND, ConnectionState.HANDSHAKING)
        register(0x00, ServerboundHandshakePacket::class.java, FlowDirection.SERVERBOUND, ConnectionState.HANDSHAKING)
        register(0x00, ServerboundStatusPacket::class.java, FlowDirection.SERVERBOUND, ConnectionState.STATUS)
    }
}