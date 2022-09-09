package us.isebas.compass.network.protocol

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import us.isebas.compass.network.FlowDirection
import us.isebas.compass.network.protocol.packet.Packet
import java.lang.reflect.InvocationTargetException
import java.util.*


enum class ConnectionState(private val map: EnumMap<FlowDirection, Int2ObjectOpenHashMap<Class<out Packet>>>) {
    HANDSHAKING(EnumMap(FlowDirection::class.java)),
    STATUS(EnumMap(FlowDirection::class.java)),
    PING(EnumMap(FlowDirection::class.java));

    open fun packets(): EnumMap<FlowDirection, Int2ObjectOpenHashMap<Class<out Packet>>> {
        return map
    }

    open fun packetById(direction: FlowDirection?, id: Int): Packet? {
        return if (!packets().computeIfAbsent(direction) { Int2ObjectOpenHashMap() }.containsKey(id)) {
            return null
        } else try {
            val packetClass = packets()[direction]!![id]
            packetClass.getDeclaredConstructor().newInstance()
        } catch (e: InstantiationException) {
            e.printStackTrace()
            return null
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            return null
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
            return null
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
            return null
        }
    }
}