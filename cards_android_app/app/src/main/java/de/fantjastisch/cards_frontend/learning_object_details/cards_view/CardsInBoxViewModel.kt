package de.fantjastisch.cards_frontend.learning_object_details.cards_view

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import de.fantjastisch.cards_frontend.learning_box.card_to_learning_box.CardToLearningBoxRepository
import kotlinx.coroutines.launch
import org.openapitools.client.models.CardEntity
import org.openapitools.client.models.ErrorEntryEntity
import java.util.*

class CardsInBoxViewModel(
    private val learningBoxId: UUID,
    private val cardRepository: CardRepository = CardRepository(),
    private val cardToLearningBoxRepository: CardToLearningBoxRepository = CardToLearningBoxRepository()
) : ViewModel() {

    val cardsInBox = mutableStateOf<List<CardEntity>>(mutableListOf())
    val errors = mutableStateOf<List<ErrorEntryEntity>>(emptyList())
    val error = mutableStateOf<String?>(null)
    val isFinished = mutableStateOf(false)


    init {
        onPageLoaded()
    }

    fun onPageLoaded() {
        viewModelScope.launch {
            val result = cardRepository.getPage(
                categoryIds = null,
                search = null,
                tag = null,
                sort = null,
            )
            when (result) {
                is RepoResult.Success -> {
                    getContainedCards(result.result)
                }
                is RepoResult.Error,
                is RepoResult.ServerError -> error.value = "Couldnt fetch cards."
            }
        }

    }

    private fun getContainedCards(allCards: List<CardEntity>) {
        cardToLearningBoxRepository.getCardIdsForBox(learningBoxId = learningBoxId,
            onSuccess = {
                cardsInBox.value = allCards.filter { card -> it.contains(card.id) }
            },
            onFailure = {
                error.value = "Couldnt get card ids for box."
            })
    }
}