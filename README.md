This abbrieviated application demonstrates some issues I've encountered with a more complex application I'm using to teach myself Kotlin and reactive programming. Both applications include the following:

* Kotlin, including Bean Definition DSL and Router Function DSL
* Spring Boot 2
* Spring Webflux
* Spring Security


### Problem 1
I have a ```BuildTestData``` class that I use to populate a MongoDB on application startup. I created a bean of type ```BCryptPasswordEncoder``` in order to encode passwords before storing them in the database.

However, when this bean exists and the curl command includes a username and password, the log shows the warning below and the server returns an HTTP 401.

```o.s.s.c.bcrypt.BCryptPasswordEncoder : Encoded password does not look like BCrypt```  

If you comment out the lines below to disable the ```BCryptPasswordEncoder``` bean and restart the application, the curl comands work as expected.

* [Beans.kt#L8](https://github.com/kensiprell/kotlin-spring-security/blob/master/src/main/kotlin/com/siprell/kotlinspringsecurity/Beans.kt#L8)
* [Handler.kt#L11](https://github.com/kensiprell/kotlin-spring-security/blob/master/src/main/kotlin/com/siprell/kotlinspringsecurity/Handler.kt#L11)
* [Handler.kt#L15](https://github.com/kensiprell/kotlin-spring-security/blob/master/src/main/kotlin/com/siprell/kotlinspringsecurity/Handler.kt#L15)

### Solution to 1

The definition below solved the ```PasswordEncoder``` problem. See [Handler.kt#L19-22](https://github.com/kensiprell/kotlin-spring-security/blob/master/src/main/kotlin/com/siprell/kotlinspringsecurity/SecurityConfiguration.kt#L19-22).

```
@Bean
fun passwordEncoder(): PasswordEncoder {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder()
}
```


## Problem 2

Trying to change the logging level for ```org.springframework.security``` in [application.yml](https://github.com/kensiprell/kotlin-spring-security/blob/master/src/main/resources/application.yml) has no effect. I've experimented with various settings without success.

### Curl Commands

```curl http://localhost:8080/encoder```

returns "class org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder" or "Disabled, depending on the bean status.

```curl http://localhost:8080/hello```

returns "Hello!"

```curl -u user:password http://localhost:8080/message```

returns "Hello, user!"

```curl -u admin:password http://localhost:8080/message```

returns "Hello, admin!"

```curl -u user:password http://localhost:8080/users/user```

returns the user details for "user"

```curl -u user:password http://localhost:8080/users/admin```

returns "Access Denied"

```curl -u admin:password http://localhost:8080/users/user```

returns the user details for "user"

```curl -u admin:password http://localhost:8080/users/admin```

returns the user details for "admin"


