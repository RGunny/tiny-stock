package me.rgunny.tinystock.common.exception.domain

open class BaseException(
    val errorCode: ErrorCode,
    message: String? = errorCode.defaultMessage
) : RuntimeException(message)

class ResourceNotFoundException(
    resourceName: String,
    resourceId: Long
) : BaseException(
    errorCode = ErrorCode.RESOURCE_NOT_FOUND,
    message = "$resourceName 에서 ID=$resourceId 을 찾을 수 없습니다."
)