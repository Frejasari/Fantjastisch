package de.fantjastisch.cards.compose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import de.fantjastisch.cards.AppDatabase
import de.fantjastisch.cards.card.Card
import de.fantjastisch.cards.card.CardRepository
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
    private val cardRepository: CardRepository = CardRepository(AppDatabase.database.cardDao()),
): ViewModel() {

    val question = mutableStateOf("")
    val answer = mutableStateOf("")

    fun onSave() {
        viewModelScope.launch {
            cardRepository.insert(Card(question=question.value, answer=answer.value))
        }
    }
}