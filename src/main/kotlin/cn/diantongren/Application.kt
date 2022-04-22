package cn.diantongren

import cn.diantongren.plugins.configureHTTP
import cn.diantongren.plugins.configureMonitoring
import cn.diantongren.plugins.configureRouting
import cn.diantongren.plugins.configureSerialization
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 9005, host = "127.0.0.1") {
        configureRouting()
//        configureSecurity()
        configureSerialization()
        configureHTTP()
        configureMonitoring()
    }.start(wait = true)
}
