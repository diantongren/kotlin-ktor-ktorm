package cn.diantongren.plugins

import cn.diantongren.util.AuthenticationException
import cn.diantongren.util.AuthorizationException
import cn.diantongren.util.ValidationException
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*

fun StatusPages.Configuration.statusPage() {
    exception<AuthenticationException> {
        call.respond(HttpStatusCode.Unauthorized)
    }
    exception<AuthorizationException> {
        call.respond(HttpStatusCode.Forbidden)
    }
    exception<ValidationException> { cause ->
        call.respond(HttpStatusCode.UnprocessableEntity, mapOf("errors" to cause.params))
    }
}