package com.example.quizzy.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quizzy.model.QuestionItem
import com.example.quizzy.screens.QuestionsViewModel
import com.example.quizzy.util.AppColors

@Composable
fun Questions(viewModel: QuestionsViewModel) {

    val questions = viewModel.data.value.data?.toMutableList()

    val questionIndex = remember {
        mutableIntStateOf(0)
    }

    if (viewModel.data.value.loading == true) {
        CircularProgressIndicator()
    }
    else{
        val question = try {
            questions?.get(questionIndex.intValue)
        }
        catch (e: Exception){
            null
        }

        if(questions != null){
            QuestionDisplay(question = question!!,
                questionIndex = questionIndex,
                viewModel = viewModel){
                questionIndex.intValue = questionIndex.intValue+1
            }
        }
    }
}

//@Preview
@Composable
fun QuestionDisplay(
    question: QuestionItem,
    questionIndex: MutableState<Int>,
    viewModel: QuestionsViewModel = hiltViewModel(),
    onNextClicked: (Int) -> Unit = {}
){
    val choicesState = listOf(question.A, question.B, question.C, question.D)

    val answerState = remember(question){
        mutableStateOf<Int?>(null)
    }

    val correctAnswerState = remember(question) {
        mutableStateOf<Boolean?>(null)
    }

    val updateAnswer: (Int) -> Unit = remember(question) {
        {
            answerState.value = it
            correctAnswerState.value = (it == (question.answer[0].code - 'A'.code))
        }
    }

    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)

    val totalQuestions = viewModel.data.value.data?.size

    Surface(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(),
        color = AppColors.mDarkPurple) {
        Column(modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start) {
            QuestionTracker(counter = questionIndex.value+1, outOf = totalQuestions!!)
            DrawDottedLine(pathEffect)

            Column {
                Text(text = question.question,
                    modifier = Modifier
                        .padding(6.dp)
                        .align(alignment = Alignment.Start)
                        .fillMaxHeight(0.3f),
                    fontSize = 17.sp,
                    color = AppColors.mOffWhite,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 27.sp
                )

                //choices
                choicesState.forEachIndexed { index, answerText ->
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .height(45.dp)
                        .border(
                            width = 4.dp, brush = Brush.linearGradient(
                                colors = listOf(AppColors.mOffDarkPurple, AppColors.mOffDarkPurple)
                            ),
                            shape = RoundedCornerShape(15.dp)
                        )
                        .clip(
                            RoundedCornerShape(
                                topStartPercent = 50,
                                topEndPercent = 50,
                                bottomStartPercent = 50,
                                bottomEndPercent = 50
                            )
                        )
                        .background(Color.Transparent)
                        .clickable {
                            updateAnswer(index)
                        },
                        verticalAlignment = Alignment.CenterVertically) {

                        if (correctAnswerState.value == true && index == answerState.value){
                            Icon(imageVector = Icons.Default.RadioButtonChecked,
                                contentDescription = "Unchecked Radio button",
                                modifier = Modifier.padding(start = 10.dp),
                                tint = Color.Green)
                        }else if(correctAnswerState.value == false && index == answerState.value){
                            Icon(imageVector = Icons.Default.RadioButtonChecked,
                                modifier = Modifier.padding(start = 10.dp),
                                contentDescription = "Unchecked Radio button",
                                tint = Color.Red)
                        } else {
                            Icon(imageVector = Icons.Default.RadioButtonUnchecked,
                                contentDescription = "Unchecked Radio button",
                                modifier = Modifier.padding(start = 10.dp),
                                tint = AppColors.mOffWhite)
                        }

                        val annotatedString = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Light,
                                color = (if (correctAnswerState.value == true
                                    && index == answerState.value){
                                    Color.Green
                                }else if(correctAnswerState.value == false
                                    && index == answerState.value){
                                    Color.Red
                                } else {
                                    AppColors.mOffWhite
                                }
                            ),
                                fontSize = 17.sp)){
                                append(answerText)
                            }
                        }
                        Text(text = annotatedString, modifier = Modifier.padding(6.dp))
                    }
                }
                Button(onClick = {
                                 onNextClicked(questionIndex.value)
                                 },
                    modifier = Modifier
                        .padding(10.dp)
                        .align(alignment = Alignment.CenterHorizontally),
                        enabled = answerState.value != null,
                        shape = RoundedCornerShape(35.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = AppColors.mLightBlue
                    )
                ) {
                    Text(text = "Next",
                        modifier = Modifier.padding(4.dp),
                        color = AppColors.mOffWhite,
                        fontSize = 17.sp)
                }
            }
        }
    }
}

@Composable
fun DrawDottedLine(pathEffect: PathEffect){
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)){
        drawLine(color = AppColors.mLightGray,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            pathEffect = pathEffect
        )
    }
}

@Preview
@Composable
fun QuestionTracker(counter: Int = 10,
                    outOf: Int = 100){
    Text(text = buildAnnotatedString {
        withStyle(style = ParagraphStyle(textIndent = TextIndent.None)){
            withStyle(style = SpanStyle(color = AppColors.mLightGray,
                fontWeight = FontWeight.Bold,
                fontSize = 27.sp
            )){
                append("Question $counter/")
                withStyle(style = SpanStyle(color = AppColors.mLightGray,
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp
                )
                ){
                    append("$outOf")
                }
            }
        }
    },
        modifier = Modifier.padding(20.dp))
}