package me.rgunny.tinystock.user.service

import me.rgunny.tinystock.user.domain.User
import me.rgunny.tinystock.user.domain.UserEntity
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

        val entity = User.toEntity(user)
        val savedEntity = userRepository.save(entity)

        return User.fromEntity(savedEntity)
    }

    fun getUserById(id: Long): User? {
        val entity = userRepository.findById(id).orElse(null) ?: return null
        return User.fromEntity(entity)
    }

    fun activateUser(id: Long): User? {
        val entity = userRepository.findById(id).orElse(null) ?: return null

        val domain = User.fromEntity(entity)
        domain.activate()

        entity.apply {
            username = domain.username
            email = domain.email
            status = domain.status
        }
        userRepository.save(entity)

        return domain
    }

    fun changeEmail(id: Long, newEmail: String): User? {
        val entity = userRepository.findById(id).orElse(null) ?: return null
        val domain = User.fromEntity(entity)

        domain.changeEmail(newEmail)

        entity.apply {
            username = domain.username
            email = domain.email
            status = domain.status
        }

        userRepository.save(entity)
        return domain
    }

}