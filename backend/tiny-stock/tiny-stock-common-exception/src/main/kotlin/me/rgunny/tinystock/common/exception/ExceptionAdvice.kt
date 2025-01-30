package me.rgunny.tinystock.common.exception

import me.rgunny.tinystock.common.exception.domain.BaseException
import me.rgunny.tinystock.common.exception.domain.ErrorCode
import me.rgunny.tinystock.common.exception.domain.ErrorResponse
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.time.Instant

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class ExceptionAdvice {

    @ExceptionHandler(BaseException::class)
    fun handleBaseException(e: BaseException): ResponseEntity<ErrorResponse> {
        val status = e.errorCode.status
        val request = currentRequest()
        val path = request?.requestURI ?: ""
        val traceId = request?.getAttribute("TRACE_ID") as? String

        val body = ErrorResponse(
            timestamp = Instant.now(),
            path = path,
            traceId = traceId,
            code = e.errorCode.code,
            message = e.message ?: e.errorCode.defaultMessage
        )
        return ResponseEntity.status(status).body(body)
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneralException(e: Exception): ResponseEntity<ErrorResponse> {
        val request = currentRequest()
        val path = request?.requestURI ?: ""
        val traceId = request?.getAttribute("TRACE_ID") as? String

        val errorCode = ErrorCode.INTERNAL_ERROR
        val body = ErrorResponse(
            timestamp = Instant.now(),
            path = path,
            traceId = traceId,
            code = errorCode.code,
            message = e.message ?: errorCode.defaultMessage
        )
        return ResponseEntity.status(errorCode.status).body(body)
    }

    private fun currentRequest() =
        (RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes)?.request
}