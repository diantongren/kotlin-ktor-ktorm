package cn.diantongren.service.impl

import cn.diantongren.dao.database
import cn.diantongren.model.Following
import cn.diantongren.model.ProfileResponse
import cn.diantongren.model.User
import cn.diantongren.model.followings
import cn.diantongren.service.ProfileService
import cn.diantongren.util.UserDoesNotExists
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.find
import org.ktorm.entity.removeIf

class ProfileServiceImpl : ProfileService {

    override suspend fun getProfile(username: String, currentUserId: Long?): ProfileResponse {

        val toUser = getUserByUsername(username) ?: return getProfileByUser(null)
        currentUserId ?: return getProfileByUser(toUser)
        val fromUser = getUser(currentUserId)
        return getProfileByUser(toUser, isFollower(toUser, fromUser))

    }

    override suspend fun changeFollowStatus(toUsername: String, fromUserId: Long, follow: Boolean): ProfileResponse {
        val toUser = getUserByUsername(toUsername) ?: throw UserDoesNotExists()
        val fromUser = getUser(fromUserId)

        if (follow) {
            addFollower(toUser, fromUser)
        } else {
            removeFollower(toUser, fromUser)
        }

        return getProfile(toUsername, fromUserId)
    }

    private fun addFollower(user: User, follower: User) {
        database.followings
            .find { (it.userId eq user.id) and (it.followerId eq follower.id) }
            ?: database.followings.add(Following {
                this.user = user
                this.follower = follower
            })
    }

    private fun removeFollower(user: User, follower: User) {
        database.followings.removeIf { (it.userId eq user.id) and (it.followerId eq follower.id) }
    }
}

fun getProfileByUser(user: User?, following: Boolean = false) =
    ProfileResponse(profile = user?.run { ProfileResponse.Profile(username, bio, image, following) })

fun isFollower(user: User, follower: User?): Boolean {
    if (follower != null) {
        database.followings.find { (it.userId eq user.id) and (it.followerId eq follower.id) } ?: return false
        return true
    }
    return false
}