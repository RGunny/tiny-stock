package me.rgunny.tinystock.user.domain

import me.rgunny.tinystock.common.exception.dto.InvalidAmountException
import me.rgunny.tinystock.common.exception.dto.NotEnoughBalanceException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UserTest {

    @BeforeEach
    fun setup() {
        val user = getInactiveUser()
    }

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
        val user = getInactiveUser()
        assertEquals(UserStatus.INACTIVE, user.status)
    }

    @Test
    fun `activate() - 비활성 상태에서 활성화`() {
        val user = getInactiveUser()
        user.activate()
        assertEquals(UserStatus.ACTIVE, user.status)
    }

    @Test
    fun `activate() - 이미 ACTIVE면 예외`() {
        val user = getActiveUser()
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
        val user = getActiveUser()
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
        val entity = getUserEntity()

        // when
        val user = UserEntity.toDomain(entity)

        // then
        assertEquals("rgunny", user.name)
        assertEquals("rgunny@test.com", user.email)
        assertEquals(UserStatus.INACTIVE, user.status)
    }

    @Test
    fun `deposit - 정상`() {
        val user = getUserWithBalance1000()
        user.deposit(500)
        assertEquals(1500, user.balance)
    }

    @Test
    fun `deposit - 0 이하`() {
        val user = getUserWithBalance1000()
        assertThrows<InvalidAmountException> {
            user.deposit(0)
        }
        assertThrows<InvalidAmountException> {
            user.deposit(-1000)
        }
    }

    @Test
    fun `withdraw - 정상`() {
        val user = getUserWithBalance1000()
        user.withdraw(300)
        assertEquals(700, user.balance)
    }

    @Test
    fun `withdraw - 0 이하`() {
        val user = getUserWithBalance1000()
        assertThrows<InvalidAmountException> {
            user.withdraw(0)
        }
    }

    @Test
    fun `withdraw - 잔고 부족`() {
        val user = getUserWithBalance1000()
        val exception = assertThrows<NotEnoughBalanceException> {
            user.withdraw(2000)
        }
        assertTrue(exception.message!!.contains("balance=1000"))
        assertEquals(1000, user.balance)
    }

    private fun getInactiveUser() = User("rgunny", "rgunny@test.com")

    private fun getActiveUser() = User("rgunny", "rgunny@test.com", UserStatus.ACTIVE)

    private fun getUserWithBalance1000() = User(name = "rgunny", email = "rgunny@test.com", balance = 1000)

    private fun getUserEntity() = UserEntity(
        id = 123L,
        name = "rgunny",
        email = "rgunny@test.com",
        status = UserStatus.INACTIVE
    )
}
