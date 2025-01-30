package me.rgunny.tinystock.common.exception.domain

import org.springframework.http.HttpStatus

data class ErrorCode(
    val status: HttpStatus,
    val code: String,
    val defaultMessage: String
) {
    companion object {
        val RESOURCE_NOT_FOUND = ErrorCode(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", "해당 리소스를 찾을 수 없습니다.")
        val INTERNAL_ERROR = ErrorCode(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "서버 내부 에러")
    }
}