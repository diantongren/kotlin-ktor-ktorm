package cn.diantongren.plugins

import cn.diantongren.util.*
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
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
    exception<UserExists> {
        call.respond(HttpStatusCode.UnprocessableEntity, mapOf("errors" to mapOf("user" to listOf("exists"))))
    }
    exception<UserDoesNotExists> {
        call.respond(HttpStatusCode.NotFound)
    }
    exception<ArticleDoesNotExist> {
        call.respond(HttpStatusCode.NotFound)
    }
    exception<MissingKotlinParameterException> { cause ->
        call.respond(
            HttpStatusCode.UnprocessableEntity,
            mapOf("errors" to mapOf(cause.parameter.name to listOf("can't be empty")))
        )
    }
    exception<CommentNotFound> {
        call.respond(HttpStatusCode.NotFound)
    }
}