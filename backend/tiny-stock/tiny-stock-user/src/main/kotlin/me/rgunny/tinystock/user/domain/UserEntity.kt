package me.rgunny.tinystock.user.domain

import jakarta.persistence.*

@Entity
@Table(name = "users")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var name: String = "",

    @Column(nullable = false, unique = true)
    var email: String = "",

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: UserStatus = UserStatus.INACTIVE,

    @Column(nullable = false)
    var balance: Long = 0
) {
    constructor() : this(null, "", "", UserStatus.INACTIVE)

    companion object {
        /**
         * User -> UserEntity (도메인 -> JPA)
         */
        fun from(user: User): UserEntity {
            return UserEntity(
                id = user.id,
                name = user.name,
                email = user.email,
                status = user.status,
                balance = user.balance
            )
        }

        /**
         * UserEntity -> User (JPA -> 도메인)
         */
        fun toDomain(entity: UserEntity): User {
            return User(
                id = entity.id,
                name = entity.name,
                email = entity.email,
                status = entity.status,
                balance = entity.balance
            )
        }
    }
}