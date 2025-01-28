package me.rgunny.tinystock.user.controller

import com.fasterxml.jackson.databind.ObjectMapper
import me.rgunny.tinystock.user.dto.UserCreateDto
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
) {

    @Test
    fun `POST - 정상적인 유저 생성`() {
        val dto = UserCreateDto(username = "rgunny", email = "rgunny@test.com")
        val body = objectMapper.writeValueAsString(dto)

        mockMvc.post("/api/users") {
            contentType = MediaType.APPLICATION_JSON
            content = body
        }
            .andExpect {
                status { isOk() }
                jsonPath("$.id") { exists() }
                jsonPath("$.username") { value("rgunny") }
                jsonPath("$.email") { value("rgunny@test.com") }
            }
    }

    @Test
    fun `POST - username이 비어있으면 400`() {
        val dto = UserCreateDto(username = "", email = "rgunny@test.com")
        val body = objectMapper.writeValueAsString(dto)

        mockMvc.post("/api/users") {
            contentType = MediaType.APPLICATION_JSON
            content = body
        }
            .andExpect {
                status { isBadRequest() }
            }
            .andDo {
                // 필요하다면 응답 본문에서 메시지 파싱
                print()
            }
    }

    @Test
    fun `POST - 이메일 형식이 잘못되면 400`() {
        val dto = UserCreateDto(username = "rgunny", email = "invalid_email")
        val body = objectMapper.writeValueAsString(dto)

        mockMvc.post("/api/users") {
            contentType = MediaType.APPLICATION_JSON
            content = body
        }
            .andExpect {
                status { isBadRequest() }
            }
    }

    @Test
    fun `GET - 존재하지 않는 ID 조회 시 404`() {
        mockMvc.get("/api/users/999999")
            .andExpect {
                status { isNotFound() }
            }
    }
}