package cn.diantongren.service.impl

import cn.diantongren.model.*
import cn.diantongren.service.ArticleService
import cn.diantongren.service.ProfileService
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.find

class ArticleServiceImpl(private val database: Database, private val profileService: ProfileService) : ArticleService {
    override suspend fun createArticle(userId: Long, newArticle: NewArticle): ArticleResponse {
//        val user = database.users.find { it.id eq userId } ?: throw UserDoesNotExists()
//        val article = Article {
//            title = newArticle.article.title
//            slug = Article.generateSlug(newArticle.article.title)
//            description = newArticle.article.description
//            body = newArticle.article.body
//            author = user
//            database.articles.add(this)
//        }
//
//        newArticle.article.tagList.map {
//            val tag = getOrCreateTag(it)
//            ArticleTag {
//                this.tag = tag
//                this.article = article
//            }
//        }

        TODO("Not yet implemented")
    }

    override suspend fun updateArticle(userId: Long, slug: String, updateArticle: UpdateArticle): ArticleResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getArticle(slug: String): ArticleResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getArticles(userId: Long?, filter: Map<String, String?>): List<ArticleResponse.Article> {
        TODO("Not yet implemented")
    }

    override suspend fun getFeedArticles(userId: Long, filter: Map<String, String?>): List<ArticleResponse.Article> {
        TODO("Not yet implemented")
    }

    override suspend fun changeFavorite(userId: Long, slug: String, favorite: Boolean): ArticleResponse {
        TODO("Not yet implemented")
    }

    override suspend fun deleteArticle(userId: Long, slug: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllTags(): TagResponse {
        TODO("Not yet implemented")
    }

    private fun getOrCreateTag(tagName: String): Tag {
        return database.tags.find { it.tagName eq tagName } ?: return Tag {
            this.tagName = tagName
            database.tags.add(this)
        }
    }
}