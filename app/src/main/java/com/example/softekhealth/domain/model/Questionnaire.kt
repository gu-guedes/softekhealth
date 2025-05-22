package com.example.softekhealth.domain.model

import java.util.Date

data class Questionnaire(
    val id: Long = 0,
    val userEmail: String,
    val date: Date,
    val stressLevel: Int, // 0-10
    val selectedSymptoms: List<String>,
    val symptomFrequency: Map<String, SymptomFrequency>,
    val notes: String? = null,
    val isDraft: Boolean = false
)

enum class SymptomFrequency {
    NEVER,
    RARELY,
    SOMETIMES,
    FREQUENTLY,
    ALWAYS
}
