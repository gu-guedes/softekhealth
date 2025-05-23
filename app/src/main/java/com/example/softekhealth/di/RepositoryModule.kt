package com.example.softekhealth.di

import com.example.softekhealth.data.repository.MoodRepositoryImpl
import com.example.softekhealth.data.repository.QuestionnaireRepositoryImpl
import com.example.softekhealth.data.repository.UserRepositoryImpl
import com.example.softekhealth.domain.repository.MoodRepository
import com.example.softekhealth.domain.repository.QuestionnaireRepository
import com.example.softekhealth.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindMoodRepository(moodRepositoryImpl: MoodRepositoryImpl): MoodRepository

    @Binds
    @Singleton
    abstract fun bindQuestionnaireRepository(questionnaireRepositoryImpl: QuestionnaireRepositoryImpl): QuestionnaireRepository
}
