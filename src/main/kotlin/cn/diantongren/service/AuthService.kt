package cn.diantongren.service

import cn.diantongren.model.RegisterUser
import cn.diantongren.model.UpdateUser
import cn.diantongren.model.User

interface AuthService {
    suspend fun register(registerUser: RegisterUser): User

    suspend fun getAllUsers(): List<User>

    suspend fun getUserByEmail(email: String): User?

    suspend fun getUserById(id: Long): User

    suspend fun loginAndGetUser(email: String, password: String): User

    suspend fun updateUser(userId: Long, updateUser: UpdateUser): User
}