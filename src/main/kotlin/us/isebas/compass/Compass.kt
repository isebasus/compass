package us.isebas.compass

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Compass

fun main(args: Array<String>) {
	runApplication<Compass>(*args)
}
