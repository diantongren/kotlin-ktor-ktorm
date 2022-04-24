package cn.diantongren.plugins

import cn.diantongren.api.auth
import cn.diantongren.service.AuthService
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject
import java.time.LocalDateTime

fun Application.configureRouting() {
    val authService: AuthService by inject()
//    val profileService: ProfileService by inject()

    routing {
        get("/pings") {
            call.respond(LocalDateTime.now())
        }

        auth(authService, simpleJWT)
//        profile(profileService)

        install(StatusPages) {
            statusPage()
        }
    }
}
