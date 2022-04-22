package cn.diantongren.plugins

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.jackson.*
import io.ktor.response.*
import io.ktor.routing.*
import org.ktorm.jackson.KtormModule

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
            registerModule(KtormModule())
            registerModule(JavaTimeModule())
        }
    }

    routing {
        get("/json/jackson") {
            call.respond(mapOf("hello" to "world"))
        }
    }
}
