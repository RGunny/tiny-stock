package me.rgunny.tinystock.user.domain

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UserTest {

    @Test
    fun `User 생성시 username, email이 비어있으면 예외`() {
        // username blank
        val blankUsernameException = assertThrows<IllegalArgumentException> {
            User("", "test@test.com")
        }
        assertTrue(blankUsernameException.message!!.contains("Username must not be blank"))

        // email blank
        val blankEmailException = assertThrows<IllegalArgumentException> {
            User("rgunny", "")
        }
        assertTrue(blankEmailException.message!!.contains("Email must not be blank"))
    }

    @Test
    fun `User 생성하면 기본 상태는 INACTIVE`() {
        val user = User("rgunny", "rgunny@test.com")
        assertEquals(UserStatus.INACTIVE, user.status)
    }

    @Test
    fun `activate() - 비활성 상태에서 활성화`() {
        val user = User("rgunny", "rgunny@test.com")
        user.activate()
        assertEquals(UserStatus.ACTIVE, user.status)
    }

    @Test
    fun `activate() - 이미 ACTIVE면 예외`() {
        val user = User("rgunny", "rgunny@test.com", UserStatus.ACTIVE)
        val alreadyActiveException = assertThrows<IllegalStateException> {
            user.activate()
        }
        assertTrue(alreadyActiveException.message!!.contains("already ACTIVE"))
    }

    @Test
    fun `deactivate() - 이미 INACTIVE면 예외`() {
        val user = User("rgunny", "rgunny@test.com", UserStatus.INACTIVE)
        val alreadyInactiveException = assertThrows<IllegalStateException> {
            user.deactivate()
        }
        assertTrue(alreadyInactiveException.message!!.contains("already INACTIVE"))
    }

    @Test
    fun `changeEmail() - 새 이메일이 blank면 예외`() {
        val user = User("rgunny", "rgunny@test.com", UserStatus.ACTIVE)
        val blankEmailException = assertThrows<IllegalArgumentException> {
            user.changeEmail("")
        }
        assertTrue(blankEmailException.message!!.contains("must not be blank"))
    }

    @Test
    fun `changeEmail() - 정상 변경`() {
        val user = User("rgunny", "old@test.com", UserStatus.ACTIVE)
        user.changeEmail("new@test.com")
        assertEquals("new@test.com", user.email)
    }

    @Test
    fun `fromEntity() - UserEntity로부터 도메인 생성`() {
        // given
        val entity = UserEntity(
            id = 123L,
            username = "rgunny",
            email = "rgunny@test.com",
            status = UserStatus.INACTIVE
        )

        // when
        val user = User.fromEntity(entity)

        // then
        assertEquals("rgunny", user.username)
        assertEquals("rgunny@test.com", user.email)
        assertEquals(UserStatus.INACTIVE, user.status)
    }

    @Test
    fun `toEntity() - 도메인으로부터 UserEntity 생성`() {
        // given
        val domain = User("rgunny", "rgunny@test.com", UserStatus.INACTIVE)

        // when
        val entity = User.toEntity(domain)

        // then
        assertEquals("rgunny", entity.username)
        assertEquals("rgunny@test.com", entity.email)
        assertEquals(UserStatus.INACTIVE, entity.status)
    }
}