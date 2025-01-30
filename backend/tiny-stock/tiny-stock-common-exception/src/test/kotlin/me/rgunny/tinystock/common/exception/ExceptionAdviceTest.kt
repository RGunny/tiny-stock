package me.rgunny.tinystock.common.exception

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.*

@SpringBootTest
@AutoConfigureMockMvc
class RestControllerAdviceTest(
    @Autowired val mockMvc: MockMvc
) {

    @Test
    fun `FakeController error - 400 with custom body`() {
        mockMvc.get("/fake/do") {
            param("arg", "error")
        }
            .andExpect {
                status { isBadRequest() }
                jsonPath("$.error") { value("IllegalState") }
                jsonPath("$.message") { value("test error") }
            }
    }

    @Test
    fun `FakeController normal - 200`() {
        mockMvc.get("/fake/do") {
            param("arg", "hello")
        }
            .andExpect {
                status { isOk() }
                content { string("OK: hello") }
            }
    }
}