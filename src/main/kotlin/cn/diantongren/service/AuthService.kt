package cn.diantongren.service

import cn.diantongren.model.RegisterUser
import cn.diantongren.model.UpdateUser
import cn.diantongren.model.User
import cn.diantongren.model.users
import cn.diantongren.util.UserDoesNotExists
import cn.diantongren.util.UserExists
import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.dsl.or
import org.ktorm.entity.add
import org.ktorm.entity.find
import org.ktorm.entity.toList

interface AuthService {
    suspend fun register(registerUser: RegisterUser): User

    suspend fun getAllUsers(): List<User>

    suspend fun getUserByEmail(email: String): User?

    suspend fun getUserById(id: Long): User

    suspend fun loginAndGetUser(email: String, password: String): User

    suspend fun updateUser(userId: Long, updateUser: UpdateUser): User
}

class AuthServiceImpl(private val database: Database) : AuthService {
    override suspend fun register(registerUser: RegisterUser): User {

        database.users.find { (it.email eq registerUser.user.email) or (it.username eq registerUser.user.username) }
            ?.let { throw UserExists() }
        val user = User {
            username = registerUser.user.username
            email = registerUser.user.email
            password = registerUser.user.password
        }
        database.users.add(user)
        println(user)
        return user
    }

    override suspend fun getAllUsers(): List<User> {
        return database.users.toList()
    }

    override suspend fun getUserByEmail(email: String): User? {
        return database.users.find { it.email eq email }
    }

    override suspend fun getUserById(id: Long): User {
        return getUser(id, database)
    }

    override suspend fun loginAndGetUser(email: String, password: String): User {
        return database.users.find { (it.email eq email) and (it.password eq password) } ?: throw UserDoesNotExists()
    }

    override suspend fun updateUser(userId: Long, updateUser: UpdateUser): User {
        val user = getUser(userId, database)
        updateUser.user.username?.let { getUserByUsername(it, database) }?.let { throw UserExists() }
        user.apply {
            password = updateUser.user.password ?: password
            username = updateUser.user.username ?: username
            image = updateUser.user.image ?: image
            bio = updateUser.user.bio ?: bio
        }
        user.flushChanges()
        return user
    }
}

fun getUser(id: Long, database: Database) = database.users.find { it.id eq id } ?: throw UserDoesNotExists()

fun getUserByUsername(username: String, database: Database) = database.users.find { it.username eq username }