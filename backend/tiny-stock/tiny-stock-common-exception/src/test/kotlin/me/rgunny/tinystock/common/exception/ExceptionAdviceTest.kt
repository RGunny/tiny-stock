package me.rgunny.tinystock.common.exception

import com.fasterxml.jackson.databind.ObjectMapper
import me.rgunny.tinystock.common.exception.domain.ErrorResponse
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.*
import org.springframework.web.bind.annotation.RestController

@SpringBootTest
@AutoConfigureMockMvc
class ExceptionAdviceTest(
    @Autowired val mockMvc: MockMvc,
    @Autowired val objectMapper: ObjectMapper
) {

    @Test
    @DisplayName("예외 발생 시 예외 어드바이스에서 에러 응답(JSON)을 반환하는지 테스트")
    fun 예외응답_테스트() {
        val result = mockMvc.get("/fake/exception/notfound") {
            param("id", "777")
        }
            .andExpect {
                status { isNotFound() }
            }
            .andReturn()

        val responseBody = result.response.contentAsString
        val errorResp = objectMapper.readValue(responseBody, ErrorResponse::class.java)

        println("=== ErrorResponse ===")
        println(errorResp)

        // 검증: code, message, timestamp, path, traceId 등
        assertEquals("RESOURCE_NOT_FOUND", errorResp.code)
        assertTrue(errorResp.message.contains("ID=777"), "메시지에 ID=777이 포함되어야 합니다.")
        assertTrue(errorResp.path.contains("/fake/exception/notfound"), "path 필드는 '/fake/exception/notfound'를 포함해야 합니다.")
        assertNotNull(errorResp.timestamp, "timestamp는 null이면 안 됩니다.")
    }
}
