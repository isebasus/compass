package us.isebas.compass.client.protocol.packet.serverbound

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import us.isebas.compass.client.util.io.OutByteBuffer

class ServerboundDisconnectPacket(private val reason: Component) : ServerboundPacket {

    override fun encode(buff: OutByteBuffer) {
        buff.writeString(PlainTextComponentSerializer.plainText().serialize(reason))
    }

}