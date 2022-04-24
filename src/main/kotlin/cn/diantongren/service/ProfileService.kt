package cn.diantongren.service

import cn.diantongren.model.ProfileResponse

interface ProfileService {

    suspend fun getProfile(username: String, currentUserId: Long? = null): ProfileResponse

    suspend fun changeFollowStatus(toUsername: String, fromUserId: Long, follow: Boolean): ProfileResponse

}