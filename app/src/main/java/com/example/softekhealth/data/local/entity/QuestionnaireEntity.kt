package com.example.softekhealth.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.softekhealth.util.Converters

@Entity(
    tableName = "questionnaires",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["email"],
            childColumns = ["userEmail"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userEmail")]
)
@TypeConverters(Converters::class)
data class QuestionnaireEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userEmail: String,
    val timestamp: Long,
    val stressLevel: Int, // 0-10
    val selectedSymptoms: List<String>, // Stored as JSON string via TypeConverter
    val symptomFrequency: Map<String, String>, // Stored as JSON string via TypeConverter
    val notes: String? = null,
    val isDraft: Boolean = false
)
