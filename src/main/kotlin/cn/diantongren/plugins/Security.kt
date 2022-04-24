package cn.diantongren.plugins

import cn.diantongren.util.SimpleJWT
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*

val simpleJWT = SimpleJWT("diantongren")

fun Application.configureSecurity() {
    install(Authentication) {
        jwt {
            authSchemes("Token")
            verifier(simpleJWT.verifier)
            validate { credential ->
                UserIdPrincipal(credential.payload.getClaim("id").asString())
            }
        }
    }
}
