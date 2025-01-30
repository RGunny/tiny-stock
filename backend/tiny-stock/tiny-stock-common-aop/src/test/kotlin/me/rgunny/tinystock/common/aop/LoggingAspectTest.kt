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
import me.rgunny.tinystock.common.aop.fake.FakeService
import org.junit.jupiter.api.DisplayName

/**
 * AOP(LoggingAspect) 테스트 예시
 * - 정상 시나리오: "[START]" / "[END]" 로그
 * - 예외 시나리오: "[ERROR]" 로그
 */
@SpringBootTest
@AutoConfigureMockMvc
class LoggingAspectTest(
    @Autowired val fakeService: FakeService
) {

    private lateinit var listAppender: ListAppender<ILoggingEvent>

    @BeforeEach
    fun 초기설정() {
        // logback-test.xml에서 MEMORY appender를 가져와 매번 초기화
        val loggerContext = LoggerFactory.getILoggerFactory() as LoggerContext
        val rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME)
        val memory = rootLogger.getAppender("MEMORY") as? ListAppender<ILoggingEvent>
        memory?.stop()
        memory?.list?.clear()
        memory?.start()
        listAppender = memory ?: error("MEMORY appender를 찾을 수 없습니다. (logback-test.xml 설정 확인)")
    }

    @Test
    @DisplayName("에러 발생 시 [ERROR] 로그를 찍는지 테스트")
    fun 에러로그_확인() {
        val ex = assertThrows<IllegalStateException> {
            fakeService.doSomething("error")
        }
        assertEquals("test error", ex.message)

        val logs = listAppender.list
        println("=== 로그 목록 (에러 시나리오) ===")
        logs.forEach { println("  level=${it.level}  msg='${it.formattedMessage}'") }

        assertTrue(
            logs.any { it.formattedMessage.contains("[ERROR]") },
            "로그에 [ERROR] 문구가 포함되어야 합니다."
        )
    }

    @Test
    @DisplayName("정상 시나리오: [START], [END] 로그가 찍히는지 테스트")
    fun 정상로그_확인() {
        val result = fakeService.doSomething("hello")
        assertEquals("OK: hello", result)

        val logs = listAppender.list
        println("=== 로그 목록 (정상 시나리오) ===")
        logs.forEach { println("  level=${it.level}  msg='${it.formattedMessage}'") }

        assertTrue(
            logs.any { it.formattedMessage.contains("[START]") },
            "[START] 로그가 찍히지 않았습니다."
        )
        assertTrue(
            logs.any { it.formattedMessage.contains("[END]") },
            "[END] 로그가 찍히지 않았습니다."
        )
    }
}