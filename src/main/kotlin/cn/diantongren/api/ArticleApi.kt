package cn.diantongren.api

import cn.diantongren.model.MultipleArticlesResponse
import cn.diantongren.model.NewArticle
import cn.diantongren.model.UpdateArticle
import cn.diantongren.service.ArticleService
import cn.diantongren.util.param
import cn.diantongren.util.userId
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
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

        /**
         *  Update Article
         *  PUT /articles/:slug
         */
        put("/articles/{slug}") {
            val slug = call.param("slug")
            val updateArticle = call.receive<UpdateArticle>()
            val article = articleService.updateArticle(call.userId(), slug, updateArticle)
            call.respond(article)
        }

        /**
         *  Favorite Article
         *  POST /articles/:slug/favorite
         */
        post("/articles/{slug}/favorite") {
            val slug = call.param("slug")
            val article = articleService.changeFavorite(call.userId(), slug, favorite = true)
            call.respond(article)
        }

        /**
         *  Unfavorite Article
         *  DELETE /articles/:slug/favorite
         */
        delete("/articles/{slug}/favorite") {
            val slug = call.param("slug")
            val article = articleService.changeFavorite(call.userId(), slug, favorite = false)
            call.respond(article)
        }

        /**
         *  Delete Article
         *  DELETE /articles/:slug
         */
        delete("/articles/{slug}") {
            val slug = call.param("slug")
            articleService.deleteArticle(call.userId(), slug)
            call.respond(HttpStatusCode.OK)
        }

    }

    authenticate(optional = true) {

        /**
         *  List Articles
         *  GET /articles
         */
        get("/articles") {
            val params = call.parameters
            val filter = mapOf(
                "tag" to params["tag"],
                "author" to params["author"],
                "favorited" to params["favorited"],
                "limit" to params["limit"],
                "offset" to params["offset"]
            )
            val articles = articleService.getArticles(call.userId(), filter)
            call.respond(MultipleArticlesResponse(articles, articles.size))
        }

    }

    /**
     *  Get Article
     *  GET /articles/:slug
     */
    get("/articles/{slug}") {
        val slug = call.param("slug")
        val article = articleService.getArticle(slug)
        call.respond(article)
    }

    /**
     *  Get Tags
     *  GET /tags
     */
    get("/tags") {
        call.respond(articleService.getAllTags())
    }

}