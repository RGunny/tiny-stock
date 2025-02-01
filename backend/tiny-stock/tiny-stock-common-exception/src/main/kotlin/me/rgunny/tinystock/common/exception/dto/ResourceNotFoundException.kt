package me.rgunny.tinystock.common.exception.dto

import me.rgunny.tinystock.common.exception.domain.BaseException
import me.rgunny.tinystock.common.exception.domain.ErrorCode

class ResourceNotFoundException(
    resourceName: String,
    resourceId: Long
) : BaseException(
    errorCode = ErrorCode.RESOURCE_NOT_FOUND,
    message = "$resourceName 에서 ID=$resourceId 을 찾을 수 없습니다."
)