package me.rgunny.tinystock.common.fake

import me.rgunny.tinystock.common.exception.dto.ResourceNotFoundException
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/fake/exception")
class FakeController(
    private val fakeService: FakeService
) {
    @GetMapping("/do")
    fun doSomething(@RequestParam arg: String): String {
        return fakeService.doSomething(arg)
    }

    @GetMapping("/notfound")
    fun resourceNotFoundException(@RequestParam id: Long) {
        throw ResourceNotFoundException("주식종목", id)
    }
}