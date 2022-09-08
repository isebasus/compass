package us.isebas.compass.network.protocol.handler

import us.isebas.compass.document.MinecraftServer
import us.isebas.compass.network.Connection
import us.isebas.compass.network.protocol.ConnectionState

class DefaultPacketHandler(private val server: MinecraftServer, private val connection: Connection){
}