package com.example.softekhealth.domain.repository

import com.example.softekhealth.domain.model.Mood
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface MoodRepository {
    suspend fun saveMood(mood: Mood): Result<Long>
    fun getAllMoodsByUser(userEmail: String): Flow<List<Mood>>
    fun getMoodsByDateRange(userEmail: String, startDate: Date, endDate: Date): Flow<List<Mood>>
    suspend fun getLatestMood(userEmail: String): Mood?
    fun getRecentMoods(userEmail: String, limit: Int): Flow<List<Mood>>
    suspend fun deleteMood(id: Long): Result<Unit>
}
