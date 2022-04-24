package cn.diantongren.plugins

import cn.diantongren.api.article
import cn.diantongren.api.auth
import cn.diantongren.api.profile
import cn.diantongren.service.ArticleService
import cn.diantongren.service.AuthService
import cn.diantongren.service.ProfileService
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject
import java.time.LocalDateTime

fun Application.configureRouting() {
    val authService: AuthService by inject()
    val profileService: ProfileService by inject()
    val articleService: ArticleService by inject()

    routing {
        get("/pings") {
            call.respond(LocalDateTime.now())
        }

        auth(authService, simpleJWT)
        profile(profileService)
        article(articleService)

        install(StatusPages) {
            statusPage()
        }
    }
}
