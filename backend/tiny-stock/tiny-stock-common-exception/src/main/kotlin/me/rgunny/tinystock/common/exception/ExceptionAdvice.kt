package me.rgunny.tinystock.common.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class RestControllerAdvice {

    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalState(ex: IllegalStateException): ResponseEntity<Any> {
        // 400 Bad Request
        val errorBody = mapOf(
            "error" to "IllegalState",
            "message" to ex.message
        )
        return ResponseEntity(errorBody, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneralException(ex: Exception): ResponseEntity<Any> {
        // 500 Internal Server Error
        val errorBody = mapOf(
            "error" to "InternalServerError",
            "message" to ex.message
        )
        return ResponseEntity(errorBody, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}