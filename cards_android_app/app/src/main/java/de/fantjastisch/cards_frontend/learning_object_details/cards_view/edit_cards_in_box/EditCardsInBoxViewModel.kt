package de.fantjastisch.cards_frontend.learning_object_details.cards_view.edit_cards_in_box

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import de.fantjastisch.cards_frontend.infrastructure.fold
import kotlinx.coroutines.launch
import org.openapitools.client.models.ErrorEntryEntity
import java.util.*

class EditCardsInBoxViewModel(
    private val learningBoxId: UUID,
    private val learningObjectId: UUID,
    private val model: EditCardsInBoxModel = EditCardsInBoxModel()
) : ViewModel() {

    val cards = mutableStateOf<List<CardSelectItem>>(mutableListOf())
    val errors = mutableStateOf<List<ErrorEntryEntity>>(emptyList())
    val error = mutableStateOf<String?>(null)
    val isFinished = mutableStateOf(false)
    var isLoading = mutableStateOf(true)

    init {
        onPageLoaded()
    }

    fun onPageLoaded() {
        viewModelScope.launch {
            model.initializePage(learningBoxId = learningBoxId, learningObjectId = learningObjectId)
                .fold(
                    onSuccess = {
                        cards.value = it
                        isLoading.value = false
                    },
                    onValidationError = { error.value = "Fehler bei der Eingabevalidierung." },
                    onUnexpectedError = { error.value = "Ein unbekannter Fehler ist aufgetreten." },
                )
        }
    }

    fun onCardSelected(id: UUID) {
        cards.value = cards.value.map {
            if (it.card.id == id) {
                it.copy(isChecked = !it.isChecked)
            } else {
                it
            }
        }
    }

    fun onAddCardsClicked() {
        val selectedCardsIds =
            cards.value
                .filter { card -> card.isChecked }
                .map { card -> card.card.id }

        viewModelScope.launch {
            model.updateCardsInBox(selectedCardsIds=selectedCardsIds, learningBoxId=learningBoxId).fold(
                onSuccess = { isFinished.value = true },
                onValidationError = { error.value = "Fehler bei der Eingabevalidierung." },
                onUnexpectedError = { error.value = "Ein unbekannter Fehler ist aufgetreten." }
            )
        }
    }
}