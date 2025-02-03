package me.rgunny.tinystock.user.service

import me.rgunny.tinystock.common.exception.dto.ResourceNotFoundException
import me.rgunny.tinystock.user.domain.User
import me.rgunny.tinystock.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository
) {

    fun createUser(user: User): User {
        val existing = userRepository.findByEmail(user.email)
        check(existing == null) { "이미 사용 중인 이메일: ${user.email}" }

        val userEntity = User.toEntity(user)
        val savedUserEntity = userRepository.save(userEntity)

        return User.fromEntity(savedUserEntity)
    }

    fun getUserById(id: Long): User? {
        val userEntity = userRepository.findById(id).orElse(null) ?: return null
        return User.fromEntity(userEntity)
    }

    fun activateUser(id: Long): User? {
        val userEntity = userRepository.findById(id).orElse(null) ?: return null

        val user = User.fromEntity(userEntity)
        user.activate()

        userEntity.apply {
            username = user.name
            email = user.email
            status = user.status
        }
        userRepository.save(userEntity)

        return user
    }

    fun changeEmail(id: Long, newEmail: String): User? {
        val userEntity = userRepository.findById(id).orElse(null) ?: return null
        val user = User.fromEntity(userEntity)

        user.changeEmail(newEmail)

        userEntity.apply {
            username = user.name
            email = user.email
            status = user.status
        }

        userRepository.save(userEntity)
        return user
    }

    fun deposit(userId: Long, amount: Long): User {
        val userEntity = userRepository.findById(userId).orElseThrow { ResourceNotFoundException("User", userId) }
        val user = User.fromEntity(userEntity)

        user.deposit(amount)
        return user
    }

    fun withdraw(userId: Long, amount: Long): User {
        val userEntity = userRepository.findById(userId).orElseThrow { ResourceNotFoundException("User", userId) }
        val user = User.fromEntity(userEntity)

        user.withdraw(amount)
        return user
    }
}