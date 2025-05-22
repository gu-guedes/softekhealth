package com.example.softekhealth.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.softekhealth.data.local.entity.QuestionnaireEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionnaireDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestionnaire(questionnaire: QuestionnaireEntity): Long
    
    @Update
    suspend fun updateQuestionnaire(questionnaire: QuestionnaireEntity)
    
    @Query("SELECT * FROM questionnaires WHERE id = :id")
    suspend fun getQuestionnaireById(id: Long): QuestionnaireEntity?
    
    @Query("SELECT * FROM questionnaires WHERE userEmail = :userEmail ORDER BY timestamp DESC")
    fun getAllQuestionnairesByUserEmail(userEmail: String): Flow<List<QuestionnaireEntity>>
    
    @Query("SELECT * FROM questionnaires WHERE userEmail = :userEmail AND isDraft = 1 ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestDraft(userEmail: String): QuestionnaireEntity?
    
    @Query("SELECT * FROM questionnaires WHERE userEmail = :userEmail AND isDraft = 0 ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentQuestionnaires(userEmail: String, limit: Int): Flow<List<QuestionnaireEntity>>
    
    @Query("DELETE FROM questionnaires WHERE id = :id")
    suspend fun deleteQuestionnaire(id: Long)
    
    @Query("SELECT * FROM questionnaires WHERE userEmail = :userEmail AND timestamp >= :startTime AND timestamp <= :endTime AND isDraft = 0 ORDER BY timestamp ASC")
    fun getQuestionnairesByTimeRange(userEmail: String, startTime: Long, endTime: Long): Flow<List<QuestionnaireEntity>>
}
