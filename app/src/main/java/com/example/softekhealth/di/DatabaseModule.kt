package com.example.softekhealth.di

import android.content.Context
import androidx.room.Room
import com.example.softekhealth.data.local.MindCompassDatabase
import com.example.softekhealth.data.local.dao.MoodDao
import com.example.softekhealth.data.local.dao.QuestionnaireDao
import com.example.softekhealth.data.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MindCompassDatabase {
        return Room.databaseBuilder(
            context,
            MindCompassDatabase::class.java,
            MindCompassDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: MindCompassDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    @Singleton
    fun provideMoodDao(database: MindCompassDatabase): MoodDao {
        return database.moodDao()
    }

    @Provides
    @Singleton
    fun provideQuestionnaireDao(database: MindCompassDatabase): QuestionnaireDao {
        return database.questionnaireDao()
    }
}
