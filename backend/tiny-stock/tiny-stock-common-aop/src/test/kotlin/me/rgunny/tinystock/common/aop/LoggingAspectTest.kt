// tiny-stock-common/src/test/kotlin/me/rgunny/tinystock/common/aop/LoggingAspectTest.kt
package me.rgunny.tinystock.common.aop

import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import me.rgunny.tinystock.common.fake.FakeService

@SpringBootTest
@AutoConfigureMockMvc
class LoggingAspectTest(
    @Autowired val fakeService: FakeService
) {

    @BeforeEach
    fun initEach() {
        // (1) 매번 ListAppender 로그 목록 초기화
        val loggerContext = LoggerFactory.getILoggerFactory() as LoggerContext
        val rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME)

        val listAppender = rootLogger.getAppender("MEMORY") as? ListAppender<ILoggingEvent>
        listAppender?.stop()
        listAppender?.list?.clear()  // 이전 테스트의 로그 비우기
        listAppender?.start()
    }

    @Test
    fun `AOP - error call logs ERROR`() {
        // 1) doSomething("error")
        val ex = assertThrows<IllegalStateException> {
            fakeService.doSomething("error")
        }
        assertEquals("test error", ex.message)

        // 2) 로그 확인
        val loggerContext = LoggerFactory.getILoggerFactory() as LoggerContext
        val rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME)
        val listAppender = rootLogger.getAppender("MEMORY") as ListAppender<ILoggingEvent>

        val events = listAppender.list
        println("Captured log events =>")
        events.forEach { println("  lvl=${it.level} msg=${it.formattedMessage}") }

        // 3) [ERROR] 메시지 검사
        assertTrue(events.any { it.formattedMessage.contains("[ERROR]") },
            "로그 중 [ERROR] 문구를 찾지 못했습니다."
        )
    }

    @Test
    fun `AOP - normal call logs START and END`() {
        // 1) doSomething("hello")
        val result = fakeService.doSomething("hello")
        assertEquals("OK: hello", result)

        // 2) 로그 확인
        val loggerContext = LoggerFactory.getILoggerFactory() as LoggerContext
        val rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME)
        val listAppender = rootLogger.getAppender("MEMORY") as ListAppender<ILoggingEvent>

        val events = listAppender.list
        println("Captured log events =>")
        events.forEach { println("  lvl=${it.level} msg=${it.formattedMessage}") }

        // 3) [START], [END] 포함 여부
        assertTrue(events.any { it.formattedMessage.contains("[START]") },
            "로그 중 [START] 문구를 찾지 못했습니다."
        )
        assertTrue(events.any { it.formattedMessage.contains("[END]") },
            "로그 중 [END] 문구를 찾지 못했습니다."
        )
    }
}