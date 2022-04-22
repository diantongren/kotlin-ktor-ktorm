package cn.diantongren.api

import cn.diantongren.model.LoginUser
import cn.diantongren.model.users
import cn.diantongren.util.param
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.entity.find
import org.ktorm.entity.toList

fun Route.user(database: Database) {
    val users = database.users
    get("/users") {
        call.respond(users.toList())
    }

    get("/users/{id}") {
        val id = call.param("id").toLong()
        val user = users.find { it.id eq id }
        call.respond("ok")
    }

    post("/users/login") {
        val loginUser = call.receive<LoginUser>()

//        users.find { it.email eq loginUser.user.email and it.password eq loginUser.user.password}
//
//        val user = authService.loginAndGetUser(loginUser.user.email, loginUser.user.password)
//        call.respond(UserResponse.fromUser(user, token = simpleJWT.sign(user.id)))
    }
}
