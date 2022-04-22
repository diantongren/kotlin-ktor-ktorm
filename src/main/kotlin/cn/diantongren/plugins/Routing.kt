package cn.diantongren.plugins

import cn.diantongren.api.user
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.response.*
import io.ktor.routing.*
import org.ktorm.database.Database
import org.ktorm.logging.ConsoleLogger
import org.ktorm.logging.LogLevel

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        val database = Database.connect(
            url = "jdbc:mysql://localhost:3306/ktorm-example?serverTimezone=UTC&useSSL=false",
            driver = "com.mysql.cj.jdbc.Driver",
            user = "root",
            password = "123456",
            logger = ConsoleLogger(threshold = LogLevel.INFO)
        )

        user(database)

        install(StatusPages) {
            statusPage()
        }
    }
}
