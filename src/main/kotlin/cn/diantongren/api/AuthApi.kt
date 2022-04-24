package cn.diantongren.api

import cn.diantongren.model.LoginUser
import cn.diantongren.model.RegisterUser
import cn.diantongren.model.UpdateUser
import cn.diantongren.model.UserResponse
import cn.diantongren.service.AuthService
import cn.diantongren.util.SimpleJWT
import cn.diantongren.util.userId
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.auth(authService: AuthService, simpleJWT: SimpleJWT) {

    /**
     * User Register
     * POST /users
     */
    post("/users") {
        val registerUser = call.receive<RegisterUser>()
        val newUser = authService.register(registerUser)
        call.respond(UserResponse.fromUser(newUser, simpleJWT.sign(newUser.id)))
    }

    /**
     * User Login
     * POST /users/login
     */
    post("/users/login") {
        val loginUser = call.receive<LoginUser>()
        val user = authService.loginAndGetUser(loginUser.user.email, loginUser.user.password)
        call.respond(UserResponse.fromUser(user, simpleJWT.sign(user.id)))
    }

    authenticate {

        /*
            Get Current User
            GET /user
         */
        get("/user") {
            val user = authService.getUserById(call.userId())
            call.respond(UserResponse.fromUser(user))
        }

        /*
            Update User
            PUT /user
         */
        put("/user") {
            val updateUser = call.receive<UpdateUser>()
            val user = authService.updateUser(call.userId(), updateUser)
            call.respond(UserResponse.fromUser(user, simpleJWT.sign(user)))
        }

    }


    //For development purposes
    //Returns all users
    get("/users") {
        val users = authService.getAllUsers()
        call.respond(users.map { UserResponse.fromUser(it) })
    }
}
