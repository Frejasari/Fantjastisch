package de.fantjastisch.cards_frontend.learning_object_details

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.update_and_create.ErrorHandlingViewModel
import de.fantjastisch.cards_frontend.learning_box.LearningBoxWithNrOfCards
import de.fantjastisch.cards_frontend.util.fold
import kotlinx.coroutines.launch
import java.util.*

/**
 * Stellt die Daten für die [LearningDetailsView] bereit und nimmt seine Anfragen entgegen.
 *
 * @property learningObjectId Die UUID des Lernobjekts, welches dargestellt werden soll.
 * @property model Das dazugehörige Model, welches die Logik kapselt.
 *
 * @author Jessica Repty, Semjon Nirmann, Freja Sender
 */
class LearningDetailsViewModel(
    val learningObjectId: UUID,
    val model: LearningDetailsModel = LearningDetailsModel()
) : ErrorHandlingViewModel() {
    val learningBoxes = mutableStateOf<List<LearningBoxWithNrOfCards>>(emptyList())
    var learningObjectLabel = ""
    val celebrate = mutableStateOf(false)


    /**
     * Lädt die internen Daten (Liste von Lernboxen mit Anzahl an Karten und die Bezeichnung)
     * eines Lernobjekts.
     *
     */
    fun onPageLoaded() {
        celebrate.value = false
        viewModelScope.launch() {
            model.initializePage(learningObjectId = learningObjectId).fold(
                onSuccess = {
                    learningBoxes.value = it.learningBoxes
                    learningObjectLabel = it.learningObjectLabel
                    val firstBoxWithCards = it.learningBoxes.firstOrNull { it.nrOfCards != 0 }
                    if (firstBoxWithCards?.boxNumber == it.learningBoxes.lastOrNull()?.boxNumber) {
                        celebrate.value = true
                    }
                }
            )
        }
    }
}