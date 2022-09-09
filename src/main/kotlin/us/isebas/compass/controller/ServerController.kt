package us.isebas.compass.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import us.isebas.compass.document.MinecraftServer
import us.isebas.compass.document.ServerStatus
import us.isebas.compass.client.NetworkController
import us.isebas.compass.service.ServerService

@RestController
@RequestMapping("v1/server")
class ServerController(@Autowired private val service: ServerService) {
    @GetMapping
    fun getAll(): ResponseEntity<List<MinecraftServer>> {
        return ResponseEntity.ok(service.findAll())
    }

    @GetMapping("/{status}")
    fun findByStatus(@PathVariable("status") status: ServerStatus): ResponseEntity<List<MinecraftServer>> {
        return ResponseEntity.ok(service.findByStatus(status))
    }

    @PostMapping
    fun save(@RequestBody server: MinecraftServer): ResponseEntity<MinecraftServer> {
        // TODO create tcp connection for Minecraft Server to get information
        val networkController = NetworkController(server)
        networkController.start()
        server.status = ServerStatus.ONLINE
        return ResponseEntity.ok(service.save(server))
    }

    @DeleteMapping
    fun delete(@RequestBody server: MinecraftServer): ResponseEntity<MinecraftServer> {
        return ResponseEntity.ok(service.save(server))
    }
}