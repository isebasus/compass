package us.isebas.compass.network.protocol.packet.serverbound

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import us.isebas.compass.network.WrappedBuff

class ServerboundDisconnectPacket(private val reason: Component) : ServerboundPacket {

    override fun encode(buff: WrappedBuff) {
        buff.writeString(PlainTextComponentSerializer.plainText().serialize(reason))
    }

}