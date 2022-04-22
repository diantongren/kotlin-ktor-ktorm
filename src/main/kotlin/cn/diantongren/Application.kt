package cn.diantongren

import cn.diantongren.plugins.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 9005, host = "127.0.0.1") {
        configureRouting()
//        configureSecurity()
        configureSerialization()
        configureHTTP()
        configureMonitoring()
        koin()
    }.start(wait = true)
}
