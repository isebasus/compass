package us.isebas.compass.service

import org.springframework.stereotype.Service
import us.isebas.compass.document.MinecraftServer
import us.isebas.compass.document.ServerStatus
import us.isebas.compass.respository.ServerRepository

@Service
class ServerService(private val serverRepository: ServerRepository) {
    fun findAll(): List<MinecraftServer> = serverRepository.findAll()

    fun findByStatus(status: ServerStatus): List<MinecraftServer> = serverRepository.findByStatus((status))

    fun save(server: MinecraftServer): MinecraftServer = serverRepository.save((server))
}