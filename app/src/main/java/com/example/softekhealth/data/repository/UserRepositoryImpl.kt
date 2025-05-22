package com.example.softekhealth.data.repository

import com.example.softekhealth.data.local.dao.UserDao
import com.example.softekhealth.data.local.entity.UserEntity
import com.example.softekhealth.domain.model.User
import com.example.softekhealth.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    override suspend fun loginUser(email: String): Result<User> {
        return try {
            // Validate email format
            if (!email.endsWith("@softtek.com")) {
                return Result.failure(IllegalArgumentException("Email deve terminar com @softtek.com"))
            }
            
            val timestamp = System.currentTimeMillis()
            val userEntity = UserEntity(email = email, lastLoginTimestamp = timestamp)
            userDao.insertUser(userEntity)
            
            Result.success(mapEntityToDomain(userEntity))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUser(email: String): User? {
        return userDao.getUserByEmail(email)?.let { mapEntityToDomain(it) }
    }

    override fun observeUser(email: String): Flow<User?> {
        return userDao.observeUserByEmail(email).map { it?.let { mapEntityToDomain(it) } }
    }

    override suspend fun logoutUser(email: String): Result<Unit> {
        return try {
            userDao.deleteUser(email)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun mapEntityToDomain(entity: UserEntity): User {
        return User(
            email = entity.email,
            lastLoginDate = Date(entity.lastLoginTimestamp)
        )
    }
}
