package de.fantjastisch.cards_frontend.learning_object_details


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import de.fantjastisch.cards_frontend.category.CategoryRepository
import de.fantjastisch.cards_frontend.glossary.CardsFilters
import de.fantjastisch.cards_frontend.learning_box.LearningBoxWitNrOfCards
import de.fantjastisch.cards_frontend.learning_mode.LearningModeFragment
import java.util.*


class LearningModeSortViewModel() : ViewModel() {

    val isFinished = mutableStateOf(false)
    val sort = mutableStateOf(false)


    fun onDismissClicked() {
        isFinished.value = true
    }
}
