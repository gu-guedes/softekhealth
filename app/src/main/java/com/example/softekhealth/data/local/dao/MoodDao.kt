package com.example.softekhealth.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.softekhealth.data.local.entity.MoodEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface MoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMood(mood: MoodEntity): Long
    
    @Query("SELECT * FROM moods WHERE userEmail = :userEmail ORDER BY timestamp DESC")
    fun getAllMoodsByUserEmail(userEmail: String): Flow<List<MoodEntity>>
    
    @Query("SELECT * FROM moods WHERE userEmail = :userEmail AND timestamp >= :startTime AND timestamp <= :endTime ORDER BY timestamp DESC")
    fun getMoodsByTimeRange(userEmail: String, startTime: Long, endTime: Long): Flow<List<MoodEntity>>
    
    @Query("SELECT * FROM moods WHERE userEmail = :userEmail ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestMood(userEmail: String): MoodEntity?
    
    @Query("SELECT * FROM moods WHERE userEmail = :userEmail ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentMoods(userEmail: String, limit: Int): Flow<List<MoodEntity>>
    
    @Query("DELETE FROM moods WHERE id = :id")
    suspend fun deleteMood(id: Long)
}
