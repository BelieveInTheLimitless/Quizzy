package com.example.quizzy.screens

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quizzy.component.Questions

@Composable
fun QuizzyHome(viewModel: QuestionsViewModel = hiltViewModel()){
    Questions(viewModel)
}