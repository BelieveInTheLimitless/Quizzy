package com.example.quizzy.di

import com.example.quizzy.network.QuestionAPI
import com.example.quizzy.repository.QuestionRepository
import com.example.quizzy.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideQuestionRepository(api: QuestionAPI) = QuestionRepository(api)

    @Singleton
    @Provides
    fun provideQuestionAPI(): QuestionAPI{
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuestionAPI::class.java)
    }
}