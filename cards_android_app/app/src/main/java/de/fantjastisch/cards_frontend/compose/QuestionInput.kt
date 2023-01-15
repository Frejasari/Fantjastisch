package de.fantjastisch.cards_frontend.compose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import de.fantjastisch.cards_frontend.learning_object.LearningObjectRepository
import de.fantjastisch.cards_frontend.config.AppDatabase
import de.fantjastisch.cards_frontend.learning_object.LearningObject
import kotlinx.coroutines.launch

class ComposeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuestionInput()
        }
    }
}

@Composable
fun QuestionInput(
    modifier: Modifier = Modifier,
) {
    val viewModel = viewModel { QuestionInputViewModel() }
    SaveLayout(
        modifier = modifier,
        innerLayout = {
            QuestionsLayout(
                question = viewModel.question,
                answer = viewModel.answer,
            )
        },
        onSave = viewModel::onSave,
    )
}

class QuestionInputViewModel(
    private val learningObjectRepository: LearningObjectRepository = LearningObjectRepository(
        AppDatabase.database.cardDao()
    ),
) : ViewModel() {

    val question = mutableStateOf("")
    val answer = mutableStateOf("")

    fun onSave() {
        viewModelScope.launch {
            learningObjectRepository.insert(
                LearningObject(
                    question = question.value,
                    answer = answer.value
                )
            )
        }
    }
}