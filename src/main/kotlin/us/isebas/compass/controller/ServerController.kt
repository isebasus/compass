package us.isebas.compass.controller

import org.springframework.data.mongodb.core.aggregation.AccumulatorOperators
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import us.isebas.compass.document.MinecraftServer
import us.isebas.compass.client.ClientController
import us.isebas.compass.controller.ApiConstants.DISHWASHER_SLEEPTIME
import us.isebas.compass.controller.ApiConstants.MAXTIME
import us.isebas.compass.controller.ApiConstants.SERVICE_SLEEPTIME
import us.isebas.compass.document.ServerError
import us.isebas.compass.document.ServerStatus
import java.lang.Error
import java.net.InetSocketAddress
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import kotlin.math.min

object ApiConstants {
    const val MAXTIME = 20000
    const val DISHWASHER_SLEEPTIME = 60000
    const val SERVICE_SLEEPTIME = 2000
}

@RestController
@CrossOrigin(origins = ["http://locahost:3000"])
class ServerController() {

    /* Used to cache data of clients */
    private var hashMap: ConcurrentHashMap<String, MinecraftServer> = ConcurrentHashMap()

    init {
        // Start dishwasher to clean up cache
        val dishwasher = getDishwasher()
        startDishwasher(dishwasher)

        // Start caching
        val cacheService = getCacheService()
        startCacheService(cacheService)
    }

    @PostMapping("v1/ping")
    fun pingServer(@RequestBody server: MinecraftServer): ResponseEntity<ServerStatus> {
        val status = ServerStatus()

        val address: String
        try {
            address = InetSocketAddress(server.hostname, server.port).toString()
        } catch (socketError: Error) {
            status.status = ServerError.NOTFOUND
            return ResponseEntity.ok(status)
        }

        val cachedServer: MinecraftServer? = hashMap[address]
        if (cachedServer == null) {
            hashMap.remove(address)

            status.status = ServerError.UNINITIALIZED
            return ResponseEntity.ok(status)
        }
        if (cachedServer.status != ServerError.SUCCESS) {
            hashMap.remove(address)

            status.status = cachedServer.status
            return ResponseEntity.ok(status)
        }

        // Return updated server information
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
        val server: MinecraftServer = minecraftServer

        val address: String
        try {
            address = InetSocketAddress(server.hostname, server.port).toString()
        } catch (socketError: Error) {
            server.status = ServerError.NOTFOUND
            return ResponseEntity.ok(server)
        }

        // Check if server is cached
        if (hashMap.contains(address)) {
            val cachedServer = hashMap[address]

            if (cachedServer != null && cachedServer.status == ServerError.SUCCESS) {
                return ResponseEntity.ok(cachedServer)
            } else {
                hashMap.remove(address)
            }
        }

        // If cache is not hit or if there is an error with the server
        getServerInfo(server)
        if (server.status != ServerError.SUCCESS) {
            return ResponseEntity.ok(server)
        }

        // Set last client ping for dishwasher
        server.lastClientPing = System.currentTimeMillis()

        if (!hashMap.contains(address)) {
            hashMap[address] = server
        }
        return ResponseEntity.ok(server)
    }

    @RequestMapping
    fun anyRequest() {
        println("Received Request");
    }

    private fun startDishwasher(dishwasher: Thread) {
        dishwasher.start()
    }

    private fun startCacheService(cacheService: Thread) {
        cacheService.start()
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

    private fun getDishwasher(): Thread {
        return Thread {
            println("Dishwasher: Started washer.")
            while (!Thread.currentThread().isInterrupted) {
                Thread.sleep(DISHWASHER_SLEEPTIME.toLong()) // Sleep for 20 seconds

                for ((key, value) in hashMap) {
                    if (System.currentTimeMillis() - value.lastClientPing > MAXTIME.toLong()) {
                        hashMap.remove(key)
                    }
                }
                println("Dishwasher: Cleaned up cache, size is: ${hashMap.size}.")
            }
        }
    }

    private fun getCacheService(): Thread {
        return Thread {
            println("Cache Service: Started service.")
            while (!Thread.currentThread().isInterrupted) {
                Thread.sleep(SERVICE_SLEEPTIME.toLong()) // Sleep for 2 seconds
                if (hashMap.size <= 0 ) {
                    continue
                }

                for ((key, server) in hashMap) {
                    if (server.status != ServerError.SUCCESS && server.status != ServerError.UNINITIALIZED) {
                        continue
                    }
                    getServerInfo(server)
                    if (server.status != ServerError.SUCCESS) {
                        hashMap[key] = server
                        continue
                    }
                    hashMap[key] = server
                    println("Cache Service: Successfully updated information for ${server.hostname}")
                }
            }
        }
    }

}