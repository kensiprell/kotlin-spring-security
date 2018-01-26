package com.siprell.kotlinspringsecurity

import org.springframework.web.reactive.function.server.router

class Routes(
    private val handler: Handler
) {
    fun router() = router {
        GET("/encoder", handler::encoder)
        GET("/hello", handler::anonymous)
        GET("/message", handler::message)
        GET("/users/{username}", handler::username)
    }
}
