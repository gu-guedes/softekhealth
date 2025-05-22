package com.example.softekhealth.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.softekhealth.data.local.dao.MoodDao
import com.example.softekhealth.data.local.dao.QuestionnaireDao
import com.example.softekhealth.data.local.dao.UserDao
import com.example.softekhealth.data.local.entity.MoodEntity
import com.example.softekhealth.data.local.entity.QuestionnaireEntity
import com.example.softekhealth.data.local.entity.UserEntity
import com.example.softekhealth.util.Converters

@Database(
    entities = [
        UserEntity::class,
        MoodEntity::class,
        QuestionnaireEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MindCompassDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun moodDao(): MoodDao
    abstract fun questionnaireDao(): QuestionnaireDao

    companion object {
        const val DATABASE_NAME = "mindcompass_db"
    }
}
