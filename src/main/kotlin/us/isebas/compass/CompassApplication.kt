package us.isebas.compass

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [
	MongoAutoConfiguration::class,
	MongoRepositoriesAutoConfiguration::class,
	MongoDataAutoConfiguration::class
])

class CompassApplication
fun main(args: Array<String>) {
	runApplication<CompassApplication>(*args)
}
