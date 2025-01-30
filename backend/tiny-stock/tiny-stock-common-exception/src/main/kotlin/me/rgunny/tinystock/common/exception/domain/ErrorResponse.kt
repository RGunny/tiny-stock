package me.rgunny.tinystock.common.exception.domain

import java.time.Instant

data class ErrorResponse(
    val timestamp: Instant = Instant.now(),
    val path: String,
    val traceId: String?,
    val code: String,
    val message: String
)