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
    private lateinit var server: MinecraftServer

    private fun getServerInfo(): ResponseEntity<MinecraftServer> {
        val clientController = ClientController(server)
        val future = clientController.start()

        return try {
            future.get(5, TimeUnit.SECONDS)
            ResponseEntity.ok(server)
        } catch (e: Exception) {
            // Return a null object
            println(e)
            val response = MinecraftServer()
            response.status = ServerStatus.NOTFOUND
            ResponseEntity.ok(response)
        }
    }

    @GetMapping
    fun getAll(): ResponseEntity<MinecraftServer> {
        if (!this::server.isInitialized) {
            // Return a null object
            println("Uninitialized server object")
            val response = MinecraftServer()
            response.status = ServerStatus.UNINITIALIZED
            return ResponseEntity.ok(response)
        }

        return getServerInfo()
    }

    @PostMapping
    fun save(@RequestBody server: MinecraftServer): ResponseEntity<MinecraftServer> {
        this.server = server
        return getServerInfo()
    }
}