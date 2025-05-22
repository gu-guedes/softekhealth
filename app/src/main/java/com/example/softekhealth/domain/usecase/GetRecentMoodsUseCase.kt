package com.example.softekhealth.domain.usecase

import com.example.softekhealth.domain.model.Mood
import com.example.softekhealth.domain.repository.MoodRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetRecentMoodsUseCase @Inject constructor(
    private val moodRepository: MoodRepository
) {
    operator fun invoke(userEmail: String, limit: Int = 14): Flow<List<Mood>> {
        if (userEmail.isBlank()) {
            return flowOf(emptyList())
        }
        
        return moodRepository.getRecentMoods(userEmail, limit)
    }
}
