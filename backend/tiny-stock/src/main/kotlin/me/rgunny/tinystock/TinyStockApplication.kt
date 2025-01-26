package me.rgunny.tinystock

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TinyStockApplication

fun main(args: Array<String>) {
    runApplication<TinyStockApplication>(*args)
}
