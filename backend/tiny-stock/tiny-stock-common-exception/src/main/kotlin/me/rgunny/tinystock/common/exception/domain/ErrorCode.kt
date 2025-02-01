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
        val INSUFFICIENT_BALANCE = ErrorCode(HttpStatus.INTERNAL_SERVER_ERROR, "INSUFFICIENT_BALANCE", "잔고를 초과한 요청입니다.")
        val INVALID_AMOUNT = ErrorCode(HttpStatus.INTERNAL_SERVER_ERROR, "INVALID_AMOUNT", "금액이 0원 이하이거나 유효하지 않습니다.")
    }
}