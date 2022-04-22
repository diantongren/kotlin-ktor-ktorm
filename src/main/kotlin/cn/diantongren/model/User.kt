package cn.diantongren.model

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.long
import org.ktorm.schema.text
import org.ktorm.schema.varchar

interface User : Entity<User> {

    companion object : Entity.Factory<User>()

    var id: Long
    var email: String
    var username: String
    var bio: String
    var image: String
    var password: String

}

object Users : Table<User>("t_user") {

    val id = long("id").primaryKey().bindTo { it.id }
    val email = varchar("email").bindTo { it.email }
    val username = varchar("username").bindTo { it.username }
    val bio = text("bio").bindTo { it.bio }
    val image = varchar("image").bindTo { it.image }
    val password = varchar("password").bindTo { it.password }

}

interface Following : Entity<Following> {

    companion object : Entity.Factory<Following>()

    var id: Long
    var user: User
    var follower: User
}

object Followings : Table<Following>("t_following") {

    val id = long("id").primaryKey().bindTo { it.id }
    val userId = long("user_id").references(Users) { it.user }
    val followingId = long("following_id").references(Users) { it.follower }

}

val Database.users get() = this.sequenceOf(Users)
val Database.followings get() = this.sequenceOf(Followings)

data class RegisterUser(val user: User) {
    data class User(val email: String, val username: String, val password: String)
}

data class LoginUser(val user: User) {
    data class User(val email: String, val password: String)
}

data class UpdateUser(val user: User) {
    data class User(
        val username: String? = null,
        val bio: String? = null,
        val image: String? = null,
        val password: String? = null
    )
}

data class UserResponse(val user: User) {
    data class User(
        val email: String,
        val token: String = "",
        val username: String,
        val bio: String,
        val image: String
    )

    companion object {
        fun fromUser(user: cn.diantongren.model.User, token: String = ""): UserResponse = UserResponse(
            user = User(
                email = user.email,
                token = token,
                username = user.username,
                bio = user.bio,
                image = user.image
            )
        )
    }
}