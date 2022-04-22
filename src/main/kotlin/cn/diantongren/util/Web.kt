package cn.diantongren.util

import io.ktor.application.*

fun ApplicationCall.param(param: String) =
    parameters[param] ?: throw ValidationException(mapOf("param" to listOf("can't be empty")))