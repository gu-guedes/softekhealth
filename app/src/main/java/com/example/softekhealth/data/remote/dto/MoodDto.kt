package com.example.softekhealth.data.remote.dto

import com.example.softekhealth.domain.model.Mood
import com.example.softekhealth.domain.model.MoodType
import java.util.Date

/**
 * DTO para transferência de dados de humor
 */
data class MoodDto(
    val id: Long = 0,
    val userEmail: String,
    val moodType: String,
    val timestamp: Long,
    val note: String? = null
) {
    fun toDomainModel(): Mood {
        return Mood(
            id = id,
            userEmail = userEmail,
            moodType = try {
                MoodType.valueOf(moodType)
            } catch (e: IllegalArgumentException) {
                MoodType.NEUTRAL
            },
            date = Date(timestamp),
            note = note
        )
    }

    companion object {
        fun fromDomainModel(mood: Mood): MoodDto {
            return MoodDto(
                id = mood.id,
                userEmail = mood.userEmail,
                moodType = mood.moodType.name,
                timestamp = mood.date.time,
                note = mood.note
            )
        }
    }
}
