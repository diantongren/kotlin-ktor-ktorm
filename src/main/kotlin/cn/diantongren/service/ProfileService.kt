package cn.diantongren.service

import cn.diantongren.model.*
import cn.diantongren.util.UserDoesNotExists
import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.find
import org.ktorm.entity.removeIf

interface ProfileService {

    suspend fun getProfile(username: String, currentUserId: Long? = null): ProfileResponse

    suspend fun changeFollowStatus(toUsername: String, fromUserId: Long, follow: Boolean): ProfileResponse

}

class ProfileServiceImpl(private val database: Database) : ProfileService {

    override suspend fun getProfile(username: String, currentUserId: Long?): ProfileResponse {
        val toUser = database.users.find { it.username eq username } ?: return getProfileByUser(null)
        currentUserId ?: return getProfileByUser(toUser)

        val fromUser = database.users.find { it.id eq currentUserId }
        return getProfileByUser(toUser, isFollower(toUser, fromUser))
    }

    override suspend fun changeFollowStatus(toUsername: String, fromUserId: Long, follow: Boolean): ProfileResponse {
        val toUser = database.users.find { it.username eq toUsername } ?: throw UserDoesNotExists()
        val fromUser = database.users.find { it.id eq fromUserId } ?: throw UserDoesNotExists()

        if (follow) {
            addFollower(toUser, fromUser)
        } else {
            removeFollower(toUser, fromUser)
        }

        return getProfile(toUsername, fromUserId)
    }

    private fun getProfileByUser(user: User?, following: Boolean = false) =
        ProfileResponse(profile = user?.run { ProfileResponse.Profile(username, bio, image, following) })

    private fun isFollower(user: User, follower: User?): Boolean {
        if (follower != null) {
            database.followings.find { (it.userId eq user.id) and (it.followerId eq follower.id) } ?: return false
            return true
        }
        return false
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