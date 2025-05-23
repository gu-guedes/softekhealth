package com.example.softekhealth.data.remote.dto

import com.example.softekhealth.domain.model.User
import java.util.Date

/**
 * DTO para transferência de dados do usuário
 */
data class UserDto(
    val email: String,
    val lastLoginTimestamp: Long
) {
    fun toDomainModel(): User {
        return User(
            email = email,
            lastLoginDate = Date(lastLoginTimestamp)
        )
    }

    companion object {
        fun fromDomainModel(user: User): UserDto {
            return UserDto(
                email = user.email,
                lastLoginTimestamp = user.lastLoginDate.time
            )
        }
    }
}
