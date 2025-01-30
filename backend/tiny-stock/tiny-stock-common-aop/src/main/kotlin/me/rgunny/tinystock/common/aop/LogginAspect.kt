package me.rgunny.tinystock.common.aop

import mu.KotlinLogging
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Aspect
@Component
class LoggingAspect {

    @Around("execution(* me.rgunny.tinystock..*(..))")
    fun logServiceMethods(joinPoint: ProceedingJoinPoint): Any? {
        val method = joinPoint.signature.toShortString()
        logger.info { "[START] $method, args=${joinPoint.args.joinToString()}" }
        val startTime = System.currentTimeMillis()
        try {
            val result = joinPoint.proceed()
            val endTime = System.currentTimeMillis()
            logger.info { "[END] $method => $result, time=${endTime - startTime}ms" }
            return result
        } catch (ex: Exception) {
            logger.error(ex) { "[ERROR] $method => ${ex.message}" }
            throw ex
        }
    }
}