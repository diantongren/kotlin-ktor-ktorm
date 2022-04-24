package cn.diantongren.model

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.*
import java.time.LocalDateTime
import java.util.*

interface Article : Entity<Article> {

    companion object : Entity.Factory<Article>() {
        fun generateSlug(title: String) = title.lowercase(Locale.US).replace(" ", "-")
    }

    var id: Long
    var slug: String
    var title: String
    var description: String
    var body: String
    var author: User
    var createdAt: LocalDateTime
    var updatedAt: LocalDateTime

}

object Articles : Table<Article>("t_article") {

    val id = long("id").primaryKey().bindTo { it.id }
    val slug = varchar("slug").bindTo { it.slug }
    val title = varchar("title").bindTo { it.title }
    val description = varchar("description").bindTo { it.description }
    val body = text("body").bindTo { it.body }
    val author = long("author").references(Users) { it.author }
    val createdAt = datetime("created_at").bindTo { it.createdAt }
    val updatedAt = datetime("updated_at").bindTo { it.updatedAt }

}

interface Tag : Entity<Tag> {

    companion object : Entity.Factory<Tag>()

    var id: Long
    var tagName: String

}

object Tags : Table<Tag>("t_tag") {

    val id = long("id").primaryKey().bindTo { it.id }
    val tagName = varchar("tag_name").bindTo { it.tagName }

}

interface ArticleTag : Entity<ArticleTag> {

    companion object : Entity.Factory<ArticleTag>()

    var id: Long
    var article: Article
    var tag: Tag

}

object ArticleTags : Table<ArticleTag>("t_article_tag") {

    val id = long("id").primaryKey().bindTo { it.id }
    val article = long("article").references(Articles) { it.article }
    val tag = long("tag").references(Tags) { it.tag }

}

interface FavoriteArticle : Entity<FavoriteArticle> {

    companion object : Entity.Factory<FavoriteArticle>()

    var id: Long
    var article: Article
    var user: User

}

object FavoriteArticles : Table<FavoriteArticle>("t_favorite_article") {

    val id = long("id").primaryKey().bindTo { it.id }
    val article = long("article").references(Articles) { it.article }
    val user = long("user").references(Users) { it.user }

}

val Database.articles get() = this.sequenceOf(Articles)
val Database.tags get() = this.sequenceOf(Tags)
val Database.articleTags get() = this.sequenceOf(ArticleTags)
val Database.favoriteTags get() = this.sequenceOf(FavoriteArticles)

data class NewArticle(val article: Article) {

    data class Article(
        val title: String,
        val description: String,
        val body: String,
        val tagList: List<String> = emptyList()
    )

}

data class UpdateArticle(val article: Article) {

    data class Article(
        val title: String? = null,
        val description: String? = null,
        val body: String? = null
    )

}

data class ArticleResponse(val article: Article) {

    data class Article(
        val slug: String,
        val title: String,
        val description: String,
        val body: String,
        val tagList: List<String>,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime,
        val favorited: Boolean = false,
        val favoritesCount: Int = 0,
        val author: ProfileResponse.Profile
    )

}

data class MultipleArticlesResponse(

    val articles: List<ArticleResponse.Article>,
    val articlesCount: Int

)

data class TagResponse(val tags: List<String>)


