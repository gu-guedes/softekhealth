package com.example.softekhealth.data.repository

import com.example.softekhealth.data.local.dao.MoodDao
import com.example.softekhealth.data.local.entity.MoodEntity
import com.example.softekhealth.domain.model.Mood
import com.example.softekhealth.domain.model.MoodType
import com.example.softekhealth.domain.repository.MoodRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class MoodRepositoryImpl @Inject constructor(
    private val moodDao: MoodDao
) : MoodRepository {

    override suspend fun saveMood(mood: Mood): Result<Long> {
        return try {
            val entity = mapDomainToEntity(mood)
            val id = moodDao.insertMood(entity)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getAllMoodsByUser(userEmail: String): Flow<List<Mood>> {
        return moodDao.getAllMoodsByUserEmail(userEmail).map { entities ->
            entities.map { mapEntityToDomain(it) }
        }
    }

    override fun getMoodsByDateRange(userEmail: String, startDate: Date, endDate: Date): Flow<List<Mood>> {
        return moodDao.getMoodsByTimeRange(
            userEmail = userEmail,
            startTime = startDate.time,
            endTime = endDate.time
        ).map { entities ->
            entities.map { mapEntityToDomain(it) }
        }
    }

    override suspend fun getLatestMood(userEmail: String): Mood? {
        return moodDao.getLatestMood(userEmail)?.let { mapEntityToDomain(it) }
    }

    override fun getRecentMoods(userEmail: String, limit: Int): Flow<List<Mood>> {
        return moodDao.getRecentMoods(userEmail, limit).map { entities ->
            entities.map { mapEntityToDomain(it) }
        }
    }

    override suspend fun deleteMood(id: Long): Result<Unit> {
        return try {
            moodDao.deleteMood(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun mapEntityToDomain(entity: MoodEntity): Mood {
        return Mood(
            id = entity.id,
            userEmail = entity.userEmail,
            moodType = getMoodTypeFromString(entity.mood),
            date = Date(entity.timestamp),
            note = entity.note
        )
    }

    private fun mapDomainToEntity(domain: Mood): MoodEntity {
        return MoodEntity(
            id = domain.id,
            userEmail = domain.userEmail,
            mood = domain.moodType.name,
            timestamp = domain.date.time,
            note = domain.note
        )
    }

    private fun getMoodTypeFromString(mood: String): MoodType {
        return try {
            MoodType.valueOf(mood)
        } catch (e: IllegalArgumentException) {
            MoodType.NEUTRAL
        }
    }
}
