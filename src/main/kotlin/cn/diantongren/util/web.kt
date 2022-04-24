package cn.diantongren.util

import io.ktor.application.*
import io.ktor.auth.*

fun ApplicationCall.userId() = principal<UserIdPrincipal>()?.name?.toLongOrNull() ?: throw AuthenticationException()

fun ApplicationCall.param(param: String) =
    parameters[param] ?: throw ValidationException(mapOf("param" to listOf("can't be empty")))