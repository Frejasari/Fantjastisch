package de.fantjastisch.cards_frontend.learning_overview.learning_object_component

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.infrastructure.fold
import kotlinx.coroutines.launch
import java.util.*

/**
 * Stellt die Daten für die [LearningObjectComponent] bereit und nimmt seine Anfragen entgegen.
 *
 * @property learningSystemId Die UUID von dem Lernsystem, welches zu einem Lernobjekt gehört.
 * @property learningObjectId Die UUID eines Lernobjekts, welches [LearningObjectComponent] darstellen will.
 * @property model Das dazugehörige Model, welches die Logik kapselt.
 *
 * @author
 */
class LearningObjectComponentViewModel(
    private val learningSystemId: UUID,
    private val learningObjectId: UUID,
    private val model: LearningObjectComponentModel = LearningObjectComponentModel()
) : ViewModel() {

    val error = mutableStateOf("")
    val progress = mutableStateOf(0)
    val learningSystemLabel = mutableStateOf("")
    val isLoading = mutableStateOf(true)
    init {
        onPageLoaded()
    }

    /**
     * Holt die Bezeichnung und den Lernfortschritt eines Lernobjekts, in dem Repository angefragt wird.
     *
     */
    private fun onPageLoaded() {
        viewModelScope.launch {
            model.initializePage(
                learningSystemId = learningSystemId,
                learningObjectId = learningObjectId
            ).fold(
                onSuccess = {
                    learningSystemLabel.value = it.learningSystemLabel
                    progress.value = it.progress
                },
                onValidationError = { error.value = "Fehler bei der Eingabevalidierung." },
                onUnexpectedError = { error.value = "Ein unbekannter Fehler ist aufgetreten." }
            )
            isLoading.value = false
        }

    }

    /**
     * Setzt die Farbe für die Darstellung des Lernfortschritts
     *
     * @return Die Farbe für den aktuellen Lernfortschritt
     */
    fun getColor() : Color {
        val color : Color
        when {
            progress.value < 33 -> color = Color(0xFFC53030)
            progress.value < 66 -> color = Color(0xFFFF8707)
            else -> color = Color(0xFF2B990D)
        }
        return color
    }
}