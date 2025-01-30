package me.rgunny.tinystock.common.fake

import org.springframework.stereotype.Service

@Service
class FakeService {
    fun doSomething(arg: String): String {
        if (arg == "error") {
            throw IllegalStateException("test error")
        }
        return "OK: $arg"
    }
}