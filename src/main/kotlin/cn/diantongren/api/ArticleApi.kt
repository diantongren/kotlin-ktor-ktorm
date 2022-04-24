package cn.diantongren.api

import cn.diantongren.model.NewArticle
import cn.diantongren.service.ArticleService
import cn.diantongren.util.userId
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.article(articleService: ArticleService) {

    authenticate {

        /**
         *  Create Article
         *  POST /articles
         */
        post("/articles") {
            val newArticle = call.receive<NewArticle>()
            val article = articleService.createArticle(call.userId(), newArticle)
            call.respond(article)
        }

    }
}