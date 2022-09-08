package us.isebas.compass.respository

import org.springframework.data.mongodb.repository.MongoRepository
import us.isebas.compass.document.MinecraftServer
import us.isebas.compass.document.ServerStatus

interface ServerRepository : MongoRepository<MinecraftServer, String> {
    fun findByStatus( status: ServerStatus) : List<MinecraftServer>
}