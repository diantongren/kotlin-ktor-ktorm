package cn.diantongren.model

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.datetime
import org.ktorm.schema.long
import org.ktorm.schema.text
import java.time.LocalDateTime

interface Comment : Entity<Comment> {

    companion object : Entity.Factory<Comment>()

    var id: Long
    var body: String
    var author: User
    var createdAt: LocalDateTime
    var updatedAt: LocalDateTime

}

object Comments : Table<Comment>("t_comment") {

    val id = long("id").primaryKey().bindTo { it.id }
    val body = text("body").bindTo { it.body }
    val author = long("author").references(Users) { it.author }
    val createdAt = datetime("created_at").bindTo { it.createdAt }
    val updatedAt = datetime("updated_at").bindTo { it.updatedAt }

}

interface ArticleComment : Entity<ArticleComment> {

    companion object : Entity.Factory<ArticleComment>()

    var id: Long
    var article: Article
    var comment: Comment

}

object ArticleComments : Table<ArticleComment>("t_article_comment") {

    val id = long("id").primaryKey().bindTo { it.id }
    val article = long("article").references(Articles) { it.article }
    val comment = long("comment").references(Comments) { it.comment }

}

val Database.comments get() = this.sequenceOf(Comments)
val Database.articleComments get() = this.sequenceOf(ArticleComments)

data class CommentResponse(val comment: Comment) {

    data class Comment(
        val id: Long,
        val body: String,
        val author: ProfileResponse.Profile,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime
    )

}

data class PostComment(val comment: Comment) {
    data class Comment(val body: String)
}