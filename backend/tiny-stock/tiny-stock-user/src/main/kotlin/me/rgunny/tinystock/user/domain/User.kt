package me.rgunny.tinystock.user.domain

class User(
    val username: String,
    var email: String,
    var status: UserStatus = UserStatus.INACTIVE,
    val id: Long? = null
) {
    init {
        require(username.isNotBlank()) { "Username must not be blank" }
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

    companion object {

        fun fromEntity(entity: UserEntity): User {
            return User(
                id = entity.id,
                username = entity.username,
                email = entity.email,
                status = entity.status
            )
        }

        fun toEntity(user: User): UserEntity {
            return UserEntity(
                id = user.id,
                username = user.username,
                email = user.email,
                status = user.status
            )
        }
    }
}