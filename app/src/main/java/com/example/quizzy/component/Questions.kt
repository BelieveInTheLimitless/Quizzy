package com.example.quizzy.component

import android.util.Log
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import com.example.quizzy.screens.QuestionsViewModel

@Composable
fun Questions(viewModel: QuestionsViewModel) {
    val questions = viewModel.data.value.data?.toMutableList()

    if (viewModel.data.value.loading == true) {
        CircularProgressIndicator()
        Log.d("loading", "Questions: Loading...")
    }
    else{
        questions?.forEach{questionItem ->
            Log.d("Result", "Questions: ${questionItem.question}")
        }
    }
}