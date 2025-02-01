package me.rgunny.tinystock.common.exception.dto

import me.rgunny.tinystock.common.exception.domain.BaseException
import me.rgunny.tinystock.common.exception.domain.ErrorCode

class NotEnoughBalanceException(
    userId: Long,
    currentBalance: Long,
    withdraw: Long
) : BaseException(
    errorCode = ErrorCode.INSUFFICIENT_BALANCE,
    message = "User($userId): balance=$currentBalance, but tried to withdraw=$withdraw"
)