package com.example.softekhealth.domain.usecase

import com.example.softekhealth.domain.model.User
import com.example.softekhealth.domain.repository.UserRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String): Result<User> {
        if (email.isBlank()) {
            return Result.failure(IllegalArgumentException("Email não pode estar em branco"))
        }
        
        if (!email.matches(Regex("^[\\w.-]+@softtek\\.com$"))) {
            return Result.failure(IllegalArgumentException("Email deve ser um email corporativo da Softtek (@softtek.com)"))
        }
        
        return userRepository.loginUser(email)
    }
}
