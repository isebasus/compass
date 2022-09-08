package us.isebas.compass.network

import io.netty.channel.Channel
import us.isebas.compass.document.MinecraftServer
import us.isebas.compass.network.protocol.ConnectionState
import us.isebas.compass.network.protocol.handler.DefaultPacketHandler

class Connection(private val server: MinecraftServer,
                 private val channel: Channel,
                 private val packetHandler: DefaultPacketHandler = DefaultPacketHandler(server, this),
                 private val state: ConnectionState = ConnectionState.HANDSHAKING)
{

}