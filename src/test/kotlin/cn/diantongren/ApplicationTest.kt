package cn.diantongren

import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.auth.*
import io.ktor.util.*
import io.ktor.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.jackson.*
import com.fasterxml.jackson.databind.*
import io.ktor.features.*
import org.slf4j.event.*
import io.ktor.request.*
import io.ktor.application.*
import io.ktor.response.*
import kotlin.test.*
import io.ktor.server.testing.*
import cn.diantongren.plugins.*

class ApplicationTest {
    @Test
    fun testRoot() {
        withTestApplication({ configureRouting() }) {
            handleRequest(HttpMethod.Get, "/").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("Hello World!", response.content)
            }
        }
    }
}