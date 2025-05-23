package com.example.softekhealth.domain.repository

import com.example.softekhealth.domain.model.Questionnaire
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface QuestionnaireRepository {
    suspend fun saveQuestionnaire(questionnaire: Questionnaire): Result<Long>
    suspend fun updateQuestionnaire(questionnaire: Questionnaire): Result<Unit>
    suspend fun getQuestionnaireById(id: Long): Questionnaire?
    fun getAllQuestionnairesByUser(userEmail: String): Flow<List<Questionnaire>>
    suspend fun getLatestDraft(userEmail: String): Questionnaire?
    fun getRecentQuestionnaires(userEmail: String, limit: Int): Flow<List<Questionnaire>>
    suspend fun deleteQuestionnaire(id: Long): Result<Unit>
    fun getQuestionnairesByDateRange(userEmail: String, startDate: Date, endDate: Date): Flow<List<Questionnaire>>
}
