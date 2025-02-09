package me.rgunny.tinystock.user.domain

import me.rgunny.tinystock.common.exception.dto.InvalidAmountException
import me.rgunny.tinystock.common.exception.dto.NotEnoughBalanceException
import me.rgunny.tinystock.user.dto.UserCreateDto

class User(
    val name: String,
    var email: String,
    var status: UserStatus = UserStatus.INACTIVE,
    var balance: Long = 0,
    val id: Long? = null
) {
    init {
        require(name.isNotBlank()) { "Username must not be blank" }
        require(email.isNotBlank()) { "Email must not be blank" }
    }

    fun activate() {
        check(status != UserStatus.ACTIVE) { "User is already ACTIVE." }
        status = UserStatus.ACTIVE
    }

    fun deactivate() {
        check(status != UserStatus.INACTIVE) { "User is already INACTIVE." }
        status = UserStatus.INACTIVE
    }

    fun changeEmail(newEmail: String) {
        require(newEmail.isNotBlank()) { "New email must not be blank." }
        this.email = newEmail
    }

    fun deposit(amount: Long) {
        if (amount <= 0) {
            throw InvalidAmountException(amount)
        }
        balance += amount
    }

    fun withdraw(amount: Long) {
        if (amount <= 0) {
            throw InvalidAmountException(amount)
        }
        if (balance < amount) {
            throw NotEnoughBalanceException(this.id ?: -1, balance, amount)
        }
        balance -= amount
    }

    companion object {

        fun from(dto: UserCreateDto): User {
            return User(
                name = dto.name,
                email = dto.email,
                // balance = 0  기본값
                // status = INACTIVE 기본값
            )
        }
    }
}