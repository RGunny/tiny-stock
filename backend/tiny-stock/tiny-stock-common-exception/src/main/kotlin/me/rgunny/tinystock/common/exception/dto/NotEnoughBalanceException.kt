package me.rgunny.tinystock.common.exception.dto

import me.rgunny.tinystock.common.exception.domain.BaseException
import me.rgunny.tinystock.common.exception.domain.ErrorCode

class NotEnoughBalanceException(
    val userId: Long,
    val currentBalance: Long,
    val withdraw: Long
) : BaseException(
    ErrorCode.INSUFFICIENT_BALANCE,
    "User($userId): balance=$currentBalance, but tried to withdraw=$withdraw"
)