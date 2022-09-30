package us.isebas.compass.document

import org.springframework.data.mongodb.core.mapping.Document

@Document
class ServerStatus {
    var playerCount: MutableList<Int> = mutableListOf()
    var ping: Long = 0
    var averagePing: MutableList<Long> = MutableList(1) {0}
    var numberOfPings: Long = 0
    var status: ServerError? = ServerError.UNINITIALIZED
}