package com.example.softekhealth.domain.repository

import com.example.softekhealth.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun loginUser(email: String): Result<User>
    suspend fun getUser(email: String): User?
    fun observeUser(email: String): Flow<User?>
    suspend fun logoutUser(email: String): Result<Unit>
}
