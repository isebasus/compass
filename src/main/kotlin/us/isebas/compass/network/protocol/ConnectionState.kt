package us.isebas.compass.network.protocol

import us.isebas.compass.network.FlowDirection
import us.isebas.compass.network.protocol.packet.Packet
import java.util.*

enum class ConnectionState(val map: EnumMap<FlowDirection, Int2ObjectOpenHashMap<Class<out Packet>>>) {
    HANDSHAKING(EnumMap(FlowDirection::class.java)),
    STATUS(EnumMap(FlowDirection::class.java)),
    PING(EnumMap(FlowDirection::class.java))
}