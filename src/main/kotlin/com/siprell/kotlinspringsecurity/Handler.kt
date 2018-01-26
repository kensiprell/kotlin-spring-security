package com.siprell.kotlinspringsecurity;

import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

class Handler(
    private var passwordEncoder: PasswordEncoder
) {

    fun encoder(serverRequest: ServerRequest): Mono<ServerResponse> {
        val password: String = passwordEncoder.encode("password")
        return ServerResponse.ok().body(Mono.just(password), String::class.java)
    }

    fun anonymous(serverRequest: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok().body(Mono.just("Hello!"), String::class.java)
    }

    fun message(serverRequest: ServerRequest): Mono<ServerResponse> {
        val principalPublisher = serverRequest.principal().map { "Hello, " + it.name + "!" }
        return ServerResponse.ok().body(principalPublisher, String::class.java)
    }

    fun username(serverRequest: ServerRequest): Mono<ServerResponse> {
        val detailsMono = serverRequest
            .principal()
            .map { p -> UserDetails::class.java.cast(Authentication::class.java.cast(p).principal) }
        return ServerResponse.ok().body(detailsMono, UserDetails::class.java)
    }
}
