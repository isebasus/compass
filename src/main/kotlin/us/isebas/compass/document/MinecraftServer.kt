package us.isebas.compass.document

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document;
import java.net.InetAddress

@Document
class MinecraftServer {
    @Id
    var id: String? = ObjectId().toHexString()
    var address: String = ""
    var port: Int = 25565
    var protocolVersion: Int = 760
    var serverVersion: String = ""
    var maxPlayerCount: Int? = null
    var playerCount: Int? = null
    var description: String = ""
    var favicon: String = ""
    var status: ServerStatus? = ServerStatus.NEW
}

enum class ServerStatus {
    NEW, ONLINE, OFFLINE
}