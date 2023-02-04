package de.fantjastisch.cards_frontend.learning_object_details.sort_dialog


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

/**
 * Stellt die Daten für das [LearningModeSortDialog] bereit und nimmt seine Anfragen entgegen.
 *
 * @author
 */
class LearningModeSortViewModel() : ViewModel() {

    val isFinished = mutableStateOf(false)
    val sort = mutableStateOf(false)

    /**
     * Setzt [isFinished] auf true, wenn der Dialog geschlossen wird.
     *
     */
    fun onDismissClicked() {
        isFinished.value = true
    }

    /**
     * Speichert den übergebenen Boolean in [sort]
     *
     * @param isAlphabetic Gibt an, ob die Karten in einer Lernbox alphabetisch sortiert werden.
     */
    fun onSortSelected(isAlphabetic: Boolean) {
        sort.value = isAlphabetic
    }
}
