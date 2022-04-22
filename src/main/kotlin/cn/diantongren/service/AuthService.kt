package cn.diantongren.service

import cn.diantongren.model.RegisterUser
import cn.diantongren.model.UpdateUser
import cn.diantongren.model.User
import cn.diantongren.model.Users
import cn.diantongren.util.UserExists
import org.ktorm.database.Database
import org.ktorm.dsl.*

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
        database.from(Users)
            .select()
            .where { (Users.email eq registerUser.user.email) or (Users.username eq registerUser.user.username) }
            .map { row -> Users.createEntity(row) }
            .firstOrNull()
            ?: throw UserExists()
        database.insertAndGenerateKey(Users) {
            set(it.username, registerUser.user.username)
            set(it.email, registerUser.user.email)
            set(it.password, registerUser.user.password)
        }
        return User {
            username = registerUser.user.username
            email = registerUser.user.email
            password = registerUser.user.password
        }
    }

    override suspend fun getAllUsers(): List<User> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserByEmail(email: String): User? {
        TODO("Not yet implemented")
    }

    override suspend fun getUserById(id: Long): User {
        TODO("Not yet implemented")
    }

    override suspend fun loginAndGetUser(email: String, password: String): User {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(userId: Long, updateUser: UpdateUser): User {
        TODO("Not yet implemented")
    }

}