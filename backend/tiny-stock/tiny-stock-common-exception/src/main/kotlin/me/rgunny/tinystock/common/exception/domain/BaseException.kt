package me.rgunny.tinystock.common.exception.domain

open class BaseException(
    val errorCode: ErrorCode,
    message: String? = errorCode.defaultMessage
) : RuntimeException(message)