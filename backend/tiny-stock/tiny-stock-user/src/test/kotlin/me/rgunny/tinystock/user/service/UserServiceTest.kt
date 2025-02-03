package me.rgunny.tinystock.user.service

import me.rgunny.tinystock.common.exception.dto.InvalidAmountException
import me.rgunny.tinystock.common.exception.dto.NotEnoughBalanceException
import me.rgunny.tinystock.common.exception.dto.ResourceNotFoundException
import me.rgunny.tinystock.user.domain.UserEntity
import me.rgunny.tinystock.user.domain.UserStatus
import me.rgunny.tinystock.user.dto.UserCreateDto
import me.rgunny.tinystock.user.repository.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserServiceTest @Autowired constructor(
    val userService: UserService,
    val userRepository: UserRepository
) {

    @BeforeEach
    fun cleanUp() {
        userRepository.deleteAll()
    }

    @Test
    fun `createUser - 신규유저의 상태는 INACTIVE`() {
        val userCreateDto = getUserCreateDto()
        val createdUser = userService.createUser(userCreateDto)

        assertEquals("rgunny", createdUser.name)
        assertEquals("rgunny@test.com", createdUser.email)
        assertEquals(UserStatus.INACTIVE, createdUser.status)
    }

    @Test
    fun `createUser - 신규유저 생성 시 중복이메일은 IllegalStateException 발생`() {
        val user1 = getUserCreateDto()
        userService.createUser(user1)

        val user2 = getUserCreateDto()
        val duplicateEmailException = assertThrows<IllegalStateException> {
            userService.createUser(user2)
        }
        assertTrue(duplicateEmailException.message!!.contains("이미 사용 중인 이메일"))
    }

    @Test
    fun `changeEmail - 이메일 변경 정상 테스트`() {
        val userCreateDto = getUserCreateDto()
        val createdUser = userService.createUser(userCreateDto)
        assertEquals("rgunny@test.com", createdUser.email)

        val updatedUser = userService.changeEmail(createdUser.id!!, "newrgunny@test.com")
        assertNotNull(updatedUser)
        assertEquals("newrgunny@test.com", updatedUser!!.email)
    }

    @Test
    fun `changeEmail - 없는 유저면 null`() {
        val user = userService.changeEmail(9999L, "rgunny@test.com")
        assertNull(user)
    }

    @Test
    fun `activateUser - DB 반영 및 도메인 로직 검증`() {
        val userCreateDto = getUserCreateDto()
        val created = userService.createUser(userCreateDto)

        assertEquals(UserStatus.INACTIVE, created.status)
        assertNotNull(created.id)

        val activated = userService.activateUser(created.id!!)

        assertNotNull(activated)
        assertEquals(UserStatus.ACTIVE, activated!!.status)
    }

    @Test
    fun `activateUser - 없는 id면 null`() {
        val result = userService.activateUser(999999)
        assertNull(result)
    }

    @Test
    fun `activateUser - 이미 ACTIVE면 예외`() {
        val userCreateDto = getUserCreateDto()

        val user = userService.createUser(userCreateDto)

        userService.activateUser(user.id!!)

        val exception = assertThrows<IllegalStateException> {
            userService.activateUser(user.id!!)
        }
        assertTrue(exception.message!!.contains("already ACTIVE"))
    }

    @Test
    fun `createUser - 정상`() {
        val userCreateDto = getUserCreateDto()
        val user = userService.createUser(userCreateDto)
        assertNotNull(user.id)
        assertEquals(0, user.balance)
    }

    @Test
    @DisplayName("입금_정상")
    fun 입금_정상() {
        val user = userRepository.save(getUserEntity())
        val updated = userService.deposit(user.id!!, 500)
        assertEquals(1500, updated.balance)
    }

    @Test
    @DisplayName("입금_유저없음_예외")
    fun 입금_유저없음_예외() {
        assertThrows<ResourceNotFoundException> {
            userService.deposit(9999, 100)
        }
    }

    @Test
    @DisplayName("출금_잔고부족_예외")
    fun 출금_잔고부족_예외() {
        val user = userRepository.save(getUserEntity())
        val ex = assertThrows<NotEnoughBalanceException> {
            userService.withdraw(user.id!!, 2000)
        }
        assertTrue(ex.message!!.contains("balance=1000"))
    }

    @Test
    @DisplayName("출금_금액0이하_예외")
    fun 출금_금액0이하_예외() {
        val user = userRepository.save(getUserEntity())
        assertThrows<InvalidAmountException> {
            userService.withdraw(user.id!!, 0)
        }
    }

    private fun getUserCreateDto() = UserCreateDto("rgunny", "rgunny@test.com")

    private fun getUserEntity() = UserEntity(email = "rgunny@test.com", name = "rgunny", balance = 1000)
}
