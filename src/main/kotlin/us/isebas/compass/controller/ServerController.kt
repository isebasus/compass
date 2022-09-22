package us.isebas.compass.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import us.isebas.compass.document.MinecraftServer
import us.isebas.compass.document.ServerStatus
import us.isebas.compass.client.ClientController
import us.isebas.compass.service.ServerService
import java.lang.Thread.sleep
import java.util.concurrent.TimeUnit

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
        val clientController = ClientController(server)
        val future = clientController.start()
        future.get(5, TimeUnit.SECONDS)

        if (server.serverVersion == "" && server.maxPlayerCount == null &&
            server.playerCount == null) {
            // Send back a null minecraft server
            return ResponseEntity.ok(MinecraftServer())
        }

        // Save server into mongodb
        return ResponseEntity.ok(service.save(server))
    }

    @DeleteMapping
    fun delete(@RequestBody server: MinecraftServer): ResponseEntity<MinecraftServer> {
        return ResponseEntity.ok(service.save(server))
    }
}