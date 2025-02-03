package me.rgunny.tinystock.user.service

import me.rgunny.tinystock.common.exception.dto.ResourceNotFoundException
import me.rgunny.tinystock.user.domain.User
import me.rgunny.tinystock.user.domain.UserEntity
import me.rgunny.tinystock.user.dto.UserCreateDto
import me.rgunny.tinystock.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository
) {

    fun createUser(userCreateDto: UserCreateDto): User {
        val existing = userRepository.findByEmail(userCreateDto.email)
        check(existing == null) { "이미 사용 중인 이메일: ${userCreateDto.email}" }

        val userEntity = UserEntity.from(User.from(userCreateDto))
        val savedUserEntity = userRepository.save(userEntity)

        return UserEntity.toDomain(savedUserEntity)
    }

    fun getUserById(id: Long): User? {
        val userEntity = userRepository.findById(id).orElse(null) ?: return null
        return UserEntity.toDomain(userEntity)
    }

    fun activateUser(id: Long): User? {
        val userEntity = userRepository.findById(id).orElse(null) ?: return null

        val user = UserEntity.toDomain(userEntity)
        user.activate()

        userEntity.apply {
            name = user.name
            email = user.email
            status = user.status
        }
        userRepository.save(userEntity)

        return user
    }

    fun changeEmail(id: Long, newEmail: String): User? {
        val userEntity = userRepository.findById(id).orElse(null) ?: return null
        val user = UserEntity.toDomain(userEntity)

        user.changeEmail(newEmail)

        userEntity.apply {
            name = user.name
            email = user.email
            status = user.status
        }

        userRepository.save(userEntity)
        return user
    }

    fun deposit(userId: Long, amount: Long): User {
        val userEntity = userRepository.findById(userId).orElseThrow { ResourceNotFoundException("User", userId) }
        val user = UserEntity.toDomain(userEntity)

        user.deposit(amount)
        return user
    }

    fun withdraw(userId: Long, amount: Long): User {
        val userEntity = userRepository.findById(userId).orElseThrow { ResourceNotFoundException("User", userId) }
        val user = UserEntity.toDomain(userEntity)

        user.withdraw(amount)
        return user
    }
}