package com.example.quizzy.network

import com.example.quizzy.model.Question
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
interface QuestionAPI {
    @GET("questions.json")
    suspend fun getAllQuestions(): Question
}