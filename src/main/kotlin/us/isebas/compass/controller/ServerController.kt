package us.isebas.compass.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import us.isebas.compass.document.MinecraftServer
import us.isebas.compass.client.ClientController
import us.isebas.compass.document.ServerError
import java.lang.Thread.sleep
import java.util.concurrent.TimeUnit

@RestController
@RequestMapping("v1/server")
class ServerController() {
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
            server.status = ServerError.NOTFOUND
            ResponseEntity.ok(server)
        }
    }

    @GetMapping
    fun getAll(): ResponseEntity<MinecraftServer> {
        if (!this::server.isInitialized) {
            // Return a null object
            println("Uninitialized server object")
            return ResponseEntity.ok(MinecraftServer())
        }

        return getServerInfo()
    }

    @PostMapping
    fun save(@RequestBody server: MinecraftServer): ResponseEntity<MinecraftServer> {
        this.server = server
        return getServerInfo()
    }

}