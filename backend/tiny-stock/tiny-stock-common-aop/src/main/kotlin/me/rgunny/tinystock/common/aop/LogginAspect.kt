package me.rgunny.tinystock.common.aop

import jakarta.servlet.http.HttpServletRequest
import mu.KotlinLogging
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.util.*

private val logger = KotlinLogging.logger {}

@Aspect
@Component
class LoggingAspect {

//    @Around("execution(* me.rgunny.tinystock..*Controller.*(..))")
    @Around("execution(* me.rgunny.tinystock..*(..))")
    fun logController(joinPoint: ProceedingJoinPoint): Any? {
        val request = currentRequest()
        // 헤더에서 X-Request-Id를 가져오거나, 없으면 새로 생성
        val traceId = getOrCreateTraceIdFromHeader(request)

        val methodSig = joinPoint.signature.toShortString()
        val paramStr = request?.parameterMap
            ?.entries
            ?.joinToString(prefix = "[", postfix = "]") { (k, v) -> "$k=(${v.joinToString(",")})" }
            ?: ""

        val path = request?.requestURI ?: "UNKNOWN"
        val host = request?.remoteHost ?: "UNKNOWN"
        val httpMethod = request?.method ?: "UNKNOWN"

        // START 로그
        logger.info { "[$traceId][START] $methodSig => $httpMethod $path $paramStr from $host" }
        val start = System.currentTimeMillis()
        return try {
            // 실제 Controller 메서드 실행
            val result = joinPoint.proceed(joinPoint.args)
            val end = System.currentTimeMillis()
            // END 로그
            logger.info { "[$traceId][END] $methodSig => result=$result, time=${end - start}ms" }
            result
        } catch (ex: Exception) {
            val end = System.currentTimeMillis()
            // ERROR 로그
            logger.error(ex) { "[$traceId][ERROR] $methodSig => ${ex.message}, time=${end - start}ms" }
            throw ex
        }
    }

    private fun currentRequest(): HttpServletRequest? {
        val attrs = RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes
        return attrs?.request
    }

    /**
     * X-Request-Id 헤더가 있으면 그 값을 traceId로 사용,
     * 없으면 새로 UUID를 생성
     */
    private fun getOrCreateTraceIdFromHeader(request: HttpServletRequest?): String {
        if (request == null) return UUID.randomUUID().toString().take(8) // fallback
        val headerValue = request.getHeader("X-Request-Id")
        return if (!headerValue.isNullOrBlank()) {
            headerValue
        } else {
            val newId = UUID.randomUUID().toString().take(8)
            newId
        }
    }
}