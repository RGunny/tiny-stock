package me.rgunny.tinystock.common.fake

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/fake")
class FakeController(
    private val fakeService: FakeService
) {
    @GetMapping("/do")
    fun doSomething(@RequestParam arg: String): String {
        return fakeService.doSomething(arg)
    }
}