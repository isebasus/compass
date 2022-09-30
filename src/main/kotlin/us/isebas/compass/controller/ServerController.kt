package us.isebas.compass.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import us.isebas.compass.document.MinecraftServer
import us.isebas.compass.client.ClientController
import us.isebas.compass.document.ServerError
import us.isebas.compass.document.ServerStatus
import java.lang.Error
import java.net.InetSocketAddress
import java.util.concurrent.TimeUnit

@RestController
@CrossOrigin(origins = ["http://locahost:3000"])
class ServerController() {

    /* Used to cache data of clients */
    private var hashMap: HashMap<String, MinecraftServer> = hashMapOf()

    private fun getServerInfo(server: MinecraftServer) {
        val clientController = ClientController(server)
        val future = clientController.start()

        try {
            future.get(5, TimeUnit.SECONDS)
        } catch (e: Exception) {
            // Return a null object
            println(e)
            server.status = ServerError.NOTFOUND
        }
    }

    @PostMapping("v1/ping")
    fun pingServer(@RequestBody server: MinecraftServer): ResponseEntity<ServerStatus> {
        val status = ServerStatus()

        val address: InetSocketAddress
        try {
            address = InetSocketAddress(server.hostname, server.port)
        } catch (socketError: Error) {
            status.status = ServerError.NOTFOUND
            return ResponseEntity.ok(status)
        }

        val cachedServer: MinecraftServer? = hashMap[address.toString()]
        if (cachedServer == null) {
            status.status = ServerError.NOTFOUND
            return ResponseEntity.ok(status)
        }

        getServerInfo(cachedServer)
        if (server.status != ServerError.SUCCESS) {
            status.status = server.status
            return ResponseEntity.ok(status)
        }
        status.playerCount = cachedServer.playerCount
        status.averagePing = cachedServer.averagePing
        status.numberOfPings = cachedServer.numberOfPings
        status.ping = cachedServer.ping
        status.status = cachedServer.status

        return ResponseEntity.ok(status)
    }

    @PostMapping("v1/init")
    fun initServer(@RequestBody server: MinecraftServer): ResponseEntity<MinecraftServer> {
        getServerInfo(server)
        if (server.status != ServerError.SUCCESS) {
            return ResponseEntity.ok(server)
        }

        val address: InetSocketAddress
        try {
            address = InetSocketAddress(server.hostname, server.port)
        } catch (socketError: Error) {
            server.status = ServerError.NOTFOUND
            return ResponseEntity.ok(server)
        }

        hashMap[address.toString()] = server
        return ResponseEntity.ok(server)
    }

    @RequestMapping
    fun anyRequest() {
        println("Received Request");
    }

}