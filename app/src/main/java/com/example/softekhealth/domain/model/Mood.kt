package com.example.softekhealth.domain.model

import java.util.Date

data class Mood(
    val id: Long = 0,
    val userEmail: String,
    val moodType: MoodType,
    val date: Date,
    val note: String? = null
)

enum class MoodType {
    HAPPY,
    CALM,
    NEUTRAL,
    SAD,
    ANGRY,
    STRESSED,
    ANXIOUS,
    TIRED,
    ENERGETIC
}
