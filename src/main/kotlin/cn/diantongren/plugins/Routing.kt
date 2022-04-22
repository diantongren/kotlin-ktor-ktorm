package cn.diantongren.plugins

import cn.diantongren.api.auth
import cn.diantongren.service.AuthService
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject
import org.ktorm.database.Database

fun Application.configureRouting() {
    val database: Database by inject()
    val authService: AuthService by inject()

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

//        val database = Database.connect(
//            url = "jdbc:mysql://localhost:3306/ktorm-example?serverTimezone=UTC&useSSL=false",
//            driver = "com.mysql.cj.jdbc.Driver",
//            user = "root",
//            password = "123456",
//            logger = ConsoleLogger(threshold = LogLevel.INFO)
//        )
        auth(authService)

        install(StatusPages) {
            statusPage()
        }
    }
}
