package me.rgunny.tinystock.user.controller

import jakarta.validation.Valid
import me.rgunny.tinystock.user.dto.UserCreateDto
import me.rgunny.tinystock.user.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {

    @PostMapping
    fun createUser(@Valid @RequestBody userCreateDto: UserCreateDto): ResponseEntity<Any> {
        val savedEntity = userService.createUser(userCreateDto)
        return ResponseEntity.ok(savedEntity)
    }

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Long): ResponseEntity<Any> {
        val user = userService.getUserById(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(user)
    }

    @PostMapping("/{id}/balance/deposit")
    fun deposit(@PathVariable id: Long, @RequestParam amount: Long): ResponseEntity<Map<String, Any>> {
        val user = userService.deposit(id, amount)
        return ResponseEntity.ok(mapOf("balance" to user.balance, "msg" to "충전 성공"))
    }

    @PostMapping("/{id}/balance/withdraw")
    fun withdraw(@PathVariable id: Long, @RequestParam amount: Long): ResponseEntity<Map<String, Any>> {
        val user = userService.withdraw(id, amount)
        return ResponseEntity.ok(mapOf("balance" to user.balance, "msg" to "잔고 출금 성공"))
    }
}