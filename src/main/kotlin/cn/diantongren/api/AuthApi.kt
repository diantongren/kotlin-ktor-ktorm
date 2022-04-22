package cn.diantongren.api

import cn.diantongren.model.RegisterUser
import cn.diantongren.model.UserResponse
import cn.diantongren.service.AuthService
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.auth(authService: AuthService) {

    post("/users") {
        val registerUser = call.receive<RegisterUser>()
        val newUser = authService.register(registerUser)
        call.respond(UserResponse.fromUser(newUser, "1234"))
    }

}
