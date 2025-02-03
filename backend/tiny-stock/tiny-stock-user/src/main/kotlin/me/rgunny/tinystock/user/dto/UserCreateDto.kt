package me.rgunny.tinystock.user.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import me.rgunny.tinystock.user.domain.User

data class UserCreateDto(
    @field:NotBlank(message = "username은 필수입니다.")
    val username: String,

    @field:NotBlank(message = "email은 필수입니다.")
    @field:Email(message = "이메일 형식이 잘못되었습니다.")
    val email: String
) {

    fun toDomain(): User {
        return User(
            name = this.username,
            email = this.email
        )
    }
}