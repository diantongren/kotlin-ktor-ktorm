package cn.diantongren.service

import cn.diantongren.model.CommentResponse
import cn.diantongren.model.PostComment

interface CommentService {
    suspend fun addComment(userId: Long, slug: String, postComment: PostComment): CommentResponse

    suspend fun getComments(userId: Long?, slug: String): List<CommentResponse.Comment>

    suspend fun deleteComment(userId: Long, slug: String, commentId: Long)
}