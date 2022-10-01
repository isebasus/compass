package us.isebas.compass.controller

import org.springframework.data.mongodb.core.aggregation.AccumulatorOperators
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import us.isebas.compass.document.MinecraftServer
import us.isebas.compass.client.ClientController
import us.isebas.compass.controller.ApiConstants.MAXTIME
import us.isebas.compass.controller.ApiConstants.SLEEPTIME
import us.isebas.compass.document.ServerError
import us.isebas.compass.document.ServerStatus
import java.lang.Error
import java.net.InetSocketAddress
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import kotlin.math.min

object ApiConstants {
    const val MAXTIME = 20000
    const val SLEEPTIME = 60000
}

@RestController
@CrossOrigin(origins = ["http://locahost:3000"])
class ServerController() {

    /* Used to cache data of clients */
    private var hashMap: ConcurrentHashMap<String, MinecraftServer> = ConcurrentHashMap()

    /* Used to clean up cache */
    init {
        val dishwasher = Thread {
            println("Dishwasher: Started washer.")
            while (!Thread.currentThread().isInterrupted) {
                Thread.sleep(SLEEPTIME.toLong())
                for ((key, value) in hashMap) {
                    if (System.currentTimeMillis() - value.lastClientPing > MAXTIME.toLong()) {
                        hashMap.remove(key)
                    }
                }
                println("Dishwasher: Cleaned up cache, size is: ${hashMap.size}.")
            }
        }
        startDishwasher(dishwasher)
    }

    private fun startDishwasher(dishwasher: Thread) {
        dishwasher.start()
    }

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
            status.status = ServerError.UNINITIALIZED
            return ResponseEntity.ok(status)
        }

        getServerInfo(cachedServer)
        if (cachedServer.status != ServerError.SUCCESS) {
            status.status = cachedServer.status
            return ResponseEntity.ok(status)
        }
        status.playerCount = ArrayList(cachedServer.playerCount)
        status.averagePing = ArrayList(cachedServer.averagePing)
        status.numberOfPings = cachedServer.numberOfPings
        status.ping = cachedServer.ping
        status.status = cachedServer.status

        cachedServer.lastClientPing = System.currentTimeMillis()
        return ResponseEntity.ok(status)
    }

    @PostMapping("v1/init")
    fun initServer(@RequestBody minecraftServer: MinecraftServer): ResponseEntity<MinecraftServer> {
        var server: MinecraftServer = minecraftServer

        val address: InetSocketAddress
        try {
            address = InetSocketAddress(server.hostname, server.port)
        } catch (socketError: Error) {
            server.status = ServerError.NOTFOUND
            return ResponseEntity.ok(server)
        }

        if (hashMap.contains(address.toString())) {
            val cachedServer = hashMap[address.toString()]
            if (cachedServer != null) {
                server = cachedServer
            }
        }

        getServerInfo(server)
        if (server.status != ServerError.SUCCESS) {
            return ResponseEntity.ok(server)
        }

        server.lastClientPing = System.currentTimeMillis()
        
        if (!hashMap.contains(address.toString())) {
            hashMap[address.toString()] = server
        }
        return ResponseEntity.ok(server)
    }

    @RequestMapping
    fun anyRequest() {
        println("Received Request");
    }

}