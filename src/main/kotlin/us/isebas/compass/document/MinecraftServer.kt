package us.isebas.compass.document

import org.springframework.data.mongodb.core.mapping.Document;

@Document
class MinecraftServer {
    var hostname: String = ""
    var port: Int = 25565
    var protocolVersion: Int = 760
    var serverVersion: String = ""
    var maxPlayerCount: Int? = null
    var playerCount: MutableList<Int> = mutableListOf()
    var description: String = ""
    var favicon: String = ""
    var ping: Long = 0
    var averagePing: MutableList<Long> = MutableList(1) {0}
    var numberOfPings: Long = 0
    var status: ServerError? = ServerError.UNINITIALIZED
}

enum class ServerError {
    SUCCESS,
    BADREQUEST,
    NOTFOUND,
    UNINITIALIZED
}