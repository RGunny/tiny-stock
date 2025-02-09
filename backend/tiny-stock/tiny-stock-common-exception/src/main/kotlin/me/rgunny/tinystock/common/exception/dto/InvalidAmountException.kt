package me.rgunny.tinystock.common.exception.dto

import me.rgunny.tinystock.common.exception.domain.BaseException
import me.rgunny.tinystock.common.exception.domain.ErrorCode

class InvalidAmountException(amount: Long) : BaseException(
    errorCode = ErrorCode.INVALID_AMOUNT,
    message = "금액($amount)이 0원 이하이거나 유효하지 않습니다."
)