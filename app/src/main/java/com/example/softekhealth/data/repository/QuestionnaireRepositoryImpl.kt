package com.example.softekhealth.data.repository

import com.example.softekhealth.data.local.dao.QuestionnaireDao
import com.example.softekhealth.data.local.entity.QuestionnaireEntity
import com.example.softekhealth.domain.model.Questionnaire
import com.example.softekhealth.domain.model.SymptomFrequency
import com.example.softekhealth.domain.repository.QuestionnaireRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class QuestionnaireRepositoryImpl @Inject constructor(
    private val questionnaireDao: QuestionnaireDao
) : QuestionnaireRepository {

    override suspend fun saveQuestionnaire(questionnaire: Questionnaire): Result<Long> {
        return try {
            val entity = mapDomainToEntity(questionnaire)
            val id = questionnaireDao.insertQuestionnaire(entity)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateQuestionnaire(questionnaire: Questionnaire): Result<Unit> {
        return try {
            val entity = mapDomainToEntity(questionnaire)
            questionnaireDao.updateQuestionnaire(entity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getQuestionnaireById(id: Long): Questionnaire? {
        return questionnaireDao.getQuestionnaireById(id)?.let { mapEntityToDomain(it) }
    }

    override fun getAllQuestionnairesByUser(userEmail: String): Flow<List<Questionnaire>> {
        return questionnaireDao.getAllQuestionnairesByUserEmail(userEmail).map { entities ->
            entities.map { mapEntityToDomain(it) }
        }
    }

    override suspend fun getLatestDraft(userEmail: String): Questionnaire? {
        return questionnaireDao.getLatestDraft(userEmail)?.let { mapEntityToDomain(it) }
    }

    override fun getRecentQuestionnaires(userEmail: String, limit: Int): Flow<List<Questionnaire>> {
        return questionnaireDao.getRecentQuestionnaires(userEmail, limit).map { entities ->
            entities.map { mapEntityToDomain(it) }
        }
    }

    override suspend fun deleteQuestionnaire(id: Long): Result<Unit> {
        return try {
            questionnaireDao.deleteQuestionnaire(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getQuestionnairesByDateRange(userEmail: String, startDate: Date, endDate: Date): Flow<List<Questionnaire>> {
        return questionnaireDao.getQuestionnairesByTimeRange(
            userEmail = userEmail,
            startTime = startDate.time,
            endTime = endDate.time
        ).map { entities ->
            entities.map { mapEntityToDomain(it) }
        }
    }

    private fun mapEntityToDomain(entity: QuestionnaireEntity): Questionnaire {
        return Questionnaire(
            id = entity.id,
            userEmail = entity.userEmail,
            date = Date(entity.timestamp),
            stressLevel = entity.stressLevel,
            selectedSymptoms = entity.selectedSymptoms,
            symptomFrequency = entity.symptomFrequency.mapValues { (_, value) ->
                try {
                    SymptomFrequency.valueOf(value)
                } catch (e: IllegalArgumentException) {
                    SymptomFrequency.NEVER
                }
            },
            notes = entity.notes,
            isDraft = entity.isDraft
        )
    }

    private fun mapDomainToEntity(domain: Questionnaire): QuestionnaireEntity {
        return QuestionnaireEntity(
            id = domain.id,
            userEmail = domain.userEmail,
            timestamp = domain.date.time,
            stressLevel = domain.stressLevel,
            selectedSymptoms = domain.selectedSymptoms,
            symptomFrequency = domain.symptomFrequency.mapValues { (_, value) -> value.name },
            notes = domain.notes,
            isDraft = domain.isDraft
        )
    }
}
