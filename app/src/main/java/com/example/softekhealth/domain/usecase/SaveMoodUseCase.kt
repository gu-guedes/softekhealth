package com.example.softekhealth.domain.usecase

import com.example.softekhealth.domain.model.Mood
import com.example.softekhealth.domain.repository.MoodRepository
import javax.inject.Inject

class SaveMoodUseCase @Inject constructor(
    private val moodRepository: MoodRepository
) {
    suspend operator fun invoke(mood: Mood): Result<Long> {
        if (mood.userEmail.isBlank()) {
            return Result.failure(IllegalArgumentException("Email do usuu00e1rio nu00e3o pode estar em branco"))
        }
        
        return moodRepository.saveMood(mood)
    }
}
