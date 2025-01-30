package me.rgunny.tinystock.user.service

import me.rgunny.tinystock.user.domain.User
import me.rgunny.tinystock.user.domain.UserStatus
import me.rgunny.tinystock.user.repository.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
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
        val user = User("rgunny", "rgunny@test.com")
        val createdUser = userService.createUser(user)

        assertEquals("rgunny", createdUser.username)
        assertEquals("rgunny@test.com", createdUser.email)
        assertEquals(UserStatus.INACTIVE, createdUser.status)
    }

    @Test
    fun `createUser - 신규유저 생성 시 중복이메일은 IllegalStateException 발생`() {
        val user1 = User("user1", "rgunny@test.com")
        userService.createUser(user1)

        val user2 = User("user2", "rgunny@test.com")
        val duplicateEmailException = assertThrows<IllegalStateException> {
            userService.createUser(user2)
        }
        assertTrue(duplicateEmailException.message!!.contains("이미 사용 중인 이메일"))
    }

    @Test
    fun `changeEmail - 이메일 변경 정상 테스트`() {
        val createdUser = userService.createUser(User("oldname", "old@test.com"))
        assertEquals("old@test.com", createdUser.email)

        val updatedUser = userService.changeEmail(createdUser.id!!, "new@test.com")
        assertNotNull(updatedUser)
        assertEquals("new@test.com", updatedUser!!.email)
    }

    @Test
    fun `changeEmail - 없는 유저면 null`() {
        val user = userService.changeEmail(9999L, "rgunny@test.com")
        assertNull(user)
    }

    @Test
    fun `activateUser - DB 반영 및 도메인 로직 검증`() {
        val created = userService.createUser(User(username = "activateUser", email = "rgunny@test.com"))

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
        val user = userService.createUser(User("alreadyActive", "rgunny@test.com", UserStatus.ACTIVE))

        val ex = assertThrows<IllegalStateException> {
            userService.activateUser(user.id!!)
        }
        assertTrue(ex.message!!.contains("already ACTIVE"))
    }
}