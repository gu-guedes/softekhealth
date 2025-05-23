package com.example.softekhealth.data.remote.dto

import com.example.softekhealth.domain.model.Questionnaire
import com.example.softekhealth.domain.model.SymptomFrequency
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

/**
 * DTO para transferu00eancia de dados de questionu00e1rio
 */
data class QuestionnaireDto(
    val id: Long = 0,
    val userEmail: String,
    val timestamp: Long,
    val stressLevel: Int,
    val selectedSymptomsJson: String,
    val symptomFrequencyJson: String,
    val notes: String? = null,
    val isDraft: Boolean = false
) {
    private val gson = Gson()

    fun toDomainModel(): Questionnaire {
        val selectedSymptoms: List<String> = gson.fromJson(
            selectedSymptomsJson,
            object : TypeToken<List<String>>() {}.type
        )

        val symptomFrequencyMap: Map<String, String> = gson.fromJson(
            symptomFrequencyJson,
            object : TypeToken<Map<String, String>>() {}.type
        )

        val domainSymptomFrequency = symptomFrequencyMap.mapValues { (_, value) ->
            try {
                SymptomFrequency.valueOf(value)
            } catch (e: IllegalArgumentException) {
                SymptomFrequency.NEVER
            }
        }

        return Questionnaire(
            id = id,
            userEmail = userEmail,
            date = Date(timestamp),
            stressLevel = stressLevel,
            selectedSymptoms = selectedSymptoms,
            symptomFrequency = domainSymptomFrequency,
            notes = notes,
            isDraft = isDraft
        )
    }

    companion object {
        fun fromDomainModel(questionnaire: Questionnaire): QuestionnaireDto {
            val gson = Gson()
            
            val selectedSymptomsJson = gson.toJson(questionnaire.selectedSymptoms)
            
            val symptomFrequencyStringMap = questionnaire.symptomFrequency.mapValues { (_, value) -> 
                value.name 
            }
            val symptomFrequencyJson = gson.toJson(symptomFrequencyStringMap)

            return QuestionnaireDto(
                id = questionnaire.id,
                userEmail = questionnaire.userEmail,
                timestamp = questionnaire.date.time,
                stressLevel = questionnaire.stressLevel,
                selectedSymptomsJson = selectedSymptomsJson,
                symptomFrequencyJson = symptomFrequencyJson,
                notes = questionnaire.notes,
                isDraft = questionnaire.isDraft
            )
        }
    }
}
