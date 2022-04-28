package cn.diantongren.service.impl

import cn.diantongren.dao.database
import cn.diantongren.model.*
import cn.diantongren.service.ArticleService
import cn.diantongren.util.ArticleDoesNotExist
import cn.diantongren.util.AuthorizationException
import cn.diantongren.util.UserDoesNotExists
import cn.diantongren.util.filterIf
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.entity.*
import java.time.LocalDateTime

class ArticleServiceImpl() : ArticleService {
    override suspend fun createArticle(userId: Long, newArticle: NewArticle): ArticleResponse {
        val user = database.users.find { it.id eq userId } ?: throw UserDoesNotExists()
        val article = Article {
            title = newArticle.article.title
            slug = Article.generateSlug(newArticle.article.title)
            description = newArticle.article.description
            body = newArticle.article.body
            author = user
            createdAt = LocalDateTime.now()
            updatedAt = LocalDateTime.now()
            database.articles.add(this)
        }

        newArticle.article.tagList.map {
            val tag = getOrCreateTag(it)
            database.articleTags.add(ArticleTag {
                this.tag = tag
                this.article = article
            })
        }

        return getArticleResponse(article, user)
    }

    override suspend fun updateArticle(userId: Long, slug: String, updateArticle: UpdateArticle): ArticleResponse {
        val user = getUser(userId)
        val article = getArticleBySlug(slug)
        if (!isArticleAuthor(article, user)) {
            throw AuthorizationException()
        }
        if (!updateArticle.article.title.isNullOrEmpty()) {
            article.apply {
                this.slug = Article.generateSlug(updateArticle.article.title)
                this.title = updateArticle.article.title
                this.body = updateArticle.article.body ?: this.body
                this.updatedAt = LocalDateTime.now()
            }
            article.flushChanges()
        }
        return getArticleResponse(article, user)
    }

    override suspend fun getArticle(slug: String): ArticleResponse {
        return getArticleResponse(getArticleBySlug(slug))
    }

    override suspend fun getArticles(userId: Long?, filter: Map<String, String?>): List<ArticleResponse.Article> {
        val user = if (userId != null) getUser(userId) else null
        return getAllArticles(
            currentUser = user,
            tag = filter["tag"],
            authorUserName = filter["author"],
            favoritedByUserName = filter["favorited"],
            limit = filter["limit"]?.toInt() ?: 20,
            offset = filter["offset"]?.toInt() ?: 0
        )
    }

    override suspend fun getFeedArticles(userId: Long, filter: Map<String, String?>): List<ArticleResponse.Article> {
        val user = getUser(userId)
        return getAllArticles(
            user,
            limit = filter["limit"]?.toInt() ?: 20,
            offset = filter["offset"]?.toInt() ?: 0,
            follows = true
        )
    }

    override suspend fun changeFavorite(userId: Long, slug: String, favorite: Boolean): ArticleResponse {
        val user = getUser(userId)
        val article = getArticleBySlug(slug)
        if (favorite) {
            favoriteArticle(article, user)
        } else {
            unFavoriteArticle(article, user)
        }

        return getArticleResponse(article, user)
    }

    override suspend fun deleteArticle(userId: Long, slug: String) {
        val user = getUser(userId)
        val article = getArticleBySlug(slug)

        if (!isArticleAuthor(article, user)) {
            throw AuthorizationException()
        }

        article.delete()
    }

    override suspend fun getAllTags(): TagResponse {
        val tabList = database.tags.map { it.tagName }
        return TagResponse(tabList)
    }

    private fun favoriteArticle(article: Article, user: User) {
        if (!isFavoritedArticle(article, user)) {
            database.favoriteArticles.add(FavoriteArticle {
                this.article = article
                this.user = user
            })
        }
    }

    private fun unFavoriteArticle(article: Article, user: User) {
        if (isFavoritedArticle(article, user)) {
            database.favoriteArticles.removeIf { (it.article eq article.id) and (it.user eq user.id) }
        }
    }

    private fun getAllArticles(
        currentUser: User? = null,
        tag: String? = null,
        authorUserName: String? = null,
        favoritedByUserName: String? = null,
        limit: Int = 20,
        offset: Int = 0,
        follows: Boolean = false
    ): List<ArticleResponse.Article> {
        val author = if (authorUserName != null) getUserByUsername(authorUserName) else null
        return database.articles
            .filterIf(author != null) { it.author eq author!!.id }
            .drop(offset).take(limit)
            .sortedByDescending { it.id }
            .toList()
            .filter {
                var flag = true

                if (favoritedByUserName != null && favoritedByUserName != "") {
                    flag = flag && isFavoritedArticle(it, getUserByUsername(favoritedByUserName))
                }

                if (tag != null) {
                    flag = flag && hasTag(tag, it)
                }

                if (follows) {
                    isFollower(it.author, currentUser)
                }

                flag
            }
            .map {
                getArticleResponse(it, currentUser).article
            }
    }
}

fun isFavoritedArticle(article: Article, user: User?) =
    if (user != null) database.favoriteArticles.any { it.article eq article.id } else false

fun isArticleAuthor(article: Article, user: User) = article.author.id == user.id

fun getOrCreateTag(tagName: String): Tag {

    return database.tags.find { it.tagName eq tagName } ?: return Tag {
        this.tagName = tagName
        database.tags.add(this)
    }

}

fun getTagByTagName(tagName: String) = database.tags.find { it.tagName eq tagName }

fun getArticleBySlug(slug: String) =
    database.articles.find { it.slug eq slug } ?: throw ArticleDoesNotExist(slug)

fun hasTag(tagName: String, article: Article) =
    database.articleTags
        .filter { it.article eq article.id }
        .toList()
        .any { it.tag.tagName == tagName }

fun getArticleResponse(article: Article, currentUser: User? = null): ArticleResponse {

    val author = article.author
    val tagList = database.articleTags.filter { it.article eq article.id }.map { it.tag.tagName }
    val favoriteCount = database.favoriteArticles.filter { it.article eq article.id }.totalRecords
    val favorited = isFavoritedArticle(article, currentUser)
    val following = isFollower(author, currentUser)
    val authorProfile = getProfileByUser(author, following).profile!!

    return ArticleResponse(
        article = ArticleResponse.Article(
            slug = article.slug,
            title = article.title,
            description = article.description,
            body = article.body,
            tagList = tagList,
            createdAt = article.createdAt,
            updatedAt = article.updatedAt,
            favorited = favorited,
            favoritesCount = favoriteCount,
            author = authorProfile
        )
    )

}


