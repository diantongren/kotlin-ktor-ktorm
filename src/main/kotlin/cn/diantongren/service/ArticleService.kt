package cn.diantongren.service

import cn.diantongren.model.ArticleResponse
import cn.diantongren.model.NewArticle
import cn.diantongren.model.TagResponse
import cn.diantongren.model.UpdateArticle

interface ArticleService {
    suspend fun createArticle(userId: Long, newArticle: NewArticle): ArticleResponse

    suspend fun updateArticle(userId: Long, slug: String, updateArticle: UpdateArticle): ArticleResponse

    suspend fun getArticle(slug: String): ArticleResponse

    suspend fun getArticles(userId: Long? = null, filter: Map<String, String?>): List<ArticleResponse.Article>

    suspend fun getFeedArticles(userId: Long, filter: Map<String, String?>): List<ArticleResponse.Article>

    suspend fun changeFavorite(userId: Long, slug: String, favorite: Boolean): ArticleResponse

    suspend fun deleteArticle(userId: Long, slug: String)

    suspend fun getAllTags(): TagResponse
}