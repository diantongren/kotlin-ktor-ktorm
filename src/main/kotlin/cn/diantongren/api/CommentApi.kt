package cn.diantongren.api

import cn.diantongren.model.PostComment
import cn.diantongren.service.CommentService
import cn.diantongren.util.param
import cn.diantongren.util.userId
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.comment(commentService: CommentService) {

    authenticate {

        /**
         *  Add Comments to an Article
         *  POST /articles/:slug/comments
         */
        post("/articles/{slug}/comments") {
            val slug = call.param("slug")
            val postComment = call.receive<PostComment>()
            val comment = commentService.addComment(call.userId(), slug, postComment)
            call.respond(comment)
        }

        /**
         *  Delete Comment
         *  DELETE /articles/:slug/comments/:id
         */
        delete("/articles/{slug}/comments/{id}") {
            val slug = call.param("slug")
            val id = call.param("id").toLong()
            commentService.deleteComment(call.userId(), slug, id)
            call.respond(HttpStatusCode.OK)
        }
    }

    authenticate(optional = true) {

        /**
         *  Get Comments from an Article
         *  GET /articles/:slug/comments
         */
        get("/articles/{slug}/comments") {
            val slug = call.param("slug")
            val comments = commentService.getComments(call.userId(), slug)
            call.respond(mapOf("comments" to comments))
        }

    }

}