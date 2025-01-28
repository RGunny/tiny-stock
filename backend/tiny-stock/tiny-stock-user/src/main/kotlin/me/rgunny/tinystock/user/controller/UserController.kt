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
    fun createUser(@Valid @RequestBody dto: UserCreateDto): ResponseEntity<Any> {
        val savedEntity = userService.createUser(dto.toDomain())
        return ResponseEntity.ok(savedEntity)
    }

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Long): ResponseEntity<Any> {
        val user = userService.getUserById(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(user)
    }
}