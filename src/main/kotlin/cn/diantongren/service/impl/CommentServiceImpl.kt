package cn.diantongren.service.impl

import cn.diantongren.dao.database
import cn.diantongren.model.*
import cn.diantongren.service.CommentService
import cn.diantongren.util.AuthorizationException
import cn.diantongren.util.CommentNotFound
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.filter
import org.ktorm.entity.find
import org.ktorm.entity.map
import java.time.LocalDateTime

class CommentServiceImpl : CommentService {
    override suspend fun addComment(userId: Long, slug: String, postComment: PostComment): CommentResponse {
        val user = getUser(userId)
        val article = getArticleBySlug(slug)
        val comment = Comment {
            body = postComment.comment.body
            author = user
            createdAt = LocalDateTime.now()
            updatedAt = LocalDateTime.now()
            database.comments.add(this)
        }
        database.articleComments.add(ArticleComment {
            this.article = article
            this.comment = comment
        })

        return getCommentResponse(comment, userId)
    }

    override suspend fun getComments(userId: Long?, slug: String): List<CommentResponse.Comment> {
        val article = getArticleBySlug(slug)
        return database.articleComments.filter { it.article eq article.id }
            .map { getCommentResponse(it.comment, userId).comment }
    }

    override suspend fun deleteComment(userId: Long, slug: String, commentId: Long) {
        val user = getUser(userId)
        val comment = getCommentById(commentId)
        val article = getArticleBySlug(slug)
        val articleComment = database.articleComments.find { (it.article eq article.id) and (it.comment eq comment.id) }
        if (comment.author.id != user.id || articleComment == null) throw AuthorizationException()
        comment.delete()
    }
}

fun getCommentResponse(comment: Comment, userId: Long?): CommentResponse {
    val author = comment.author
    val currentUser = if (userId != null) getUser(userId) else null
    val following = isFollower(author, currentUser)
    val authorProfile = getProfileByUser(author, following)
    return CommentResponse(
        comment = CommentResponse.Comment(
            id = comment.id,
            body = comment.body,
            author = authorProfile.profile!!,
            createdAt = comment.createdAt,
            updatedAt = comment.updatedAt
        )
    )
}

fun getCommentById(id: Long) = database.comments.find { it.id eq id } ?: throw CommentNotFound()