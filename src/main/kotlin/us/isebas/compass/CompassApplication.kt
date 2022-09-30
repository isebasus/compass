package us.isebas.compass

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.filter.CommonsRequestLoggingFilter
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@SpringBootApplication(exclude = [
	MongoAutoConfiguration::class,
	MongoRepositoriesAutoConfiguration::class,
	MongoDataAutoConfiguration::class
])

class CompassApplication
fun main(args: Array<String>) {
	runApplication<CompassApplication>(*args)
}

