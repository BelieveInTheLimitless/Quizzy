package com.example.quizzy.repository

import android.util.Log
import com.example.quizzy.data.DataOrException
import com.example.quizzy.model.QuestionItem
import com.example.quizzy.network.QuestionAPI
import javax.inject.Inject

class QuestionRepository @Inject constructor(
    private  val api: QuestionAPI
) {
    private val dataOrException = DataOrException<ArrayList<QuestionItem>, Boolean, Exception>()

    suspend fun getAllQuestions(): DataOrException<ArrayList<QuestionItem>, Boolean, Exception>{
        try {
            dataOrException.loading = true
            dataOrException.data = api.getAllQuestions()
            if (dataOrException.data.toString().isNotEmpty()) dataOrException.loading = false
        }
        catch (exception: Exception){
            dataOrException.e = exception
            Log.d("Exception", "getAllQuestions: ${dataOrException.e!!.localizedMessage}")
        }
        return dataOrException
    }
}