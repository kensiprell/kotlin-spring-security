package com.siprell.kotlinspringsecurity

import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

fun beans() = org.springframework.context.support.beans {
    bean<BCryptPasswordEncoder>()
    bean<Handler>()
    bean {
        Routes(
            ref<Handler>()
        ).router()
    }
}

class BeansInitializer : ApplicationContextInitializer<GenericApplicationContext> {
    override fun initialize(context: GenericApplicationContext) = beans().initialize(context)
}
