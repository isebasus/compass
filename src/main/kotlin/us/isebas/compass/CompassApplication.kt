package us.isebas.compass

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CompassApplication

fun main(args: Array<String>) {
	runApplication<CompassApplication>(*args)
}
