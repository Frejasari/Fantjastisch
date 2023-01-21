package de.fantjastisch.cards_frontend.learning_object_details.cards_view.edit_cards_in_box

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.config.AppDatabase
import de.fantjastisch.cards_frontend.learning_box.card_to_learning_box.CardToLearningBoxRepository
import de.fantjastisch.cards_frontend.learning_box.card_to_learning_box.InternalCardToLearningBoxRepository
import org.openapitools.client.models.CardEntity
import org.openapitools.client.models.ErrorEntryEntity
import java.util.*

class AddCardsToBoxViewModel(
    private val learningBoxId: UUID,
    private val cardRepository: CardRepository = CardRepository(),
    private val cardToLearningBoxRepository: CardToLearningBoxRepository = CardToLearningBoxRepository(
        InternalCardToLearningBoxRepository(AppDatabase.database.cardToLearningBoxDao())
    )
) : ViewModel() {

    val cards = mutableStateOf<List<CardSelectItem>>(mutableListOf())
    val errors = mutableStateOf<List<ErrorEntryEntity>>(emptyList())
    val error = mutableStateOf<String?>(null)
    val isFinished = mutableStateOf(false)


    init {
        onPageLoaded()
    }

    fun onPageLoaded() {
        cardRepository.getPage(
            categoryIds = null,
            search = null,
            tag = null,
            sort = null,
            onSuccess = {
                getContainedCards(it)
            },
            onFailure = { error.value = "Couldnt fetch cards." }
        )


    }

    private fun getContainedCards(allCards: List<CardEntity>) {
        cardToLearningBoxRepository.getCardIdsForBox(learningBoxId = learningBoxId,
            onSuccess = {
                cards.value = allCards.map { card -> CardSelectItem(card=card, isChecked = it.contains(card.id)) }
            },
            onFailure = {
                error.value = "Couldnt get card ids for box."
            })
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
        val selectedCardsIds = cards.value.filter {card -> card.isChecked}.map { card -> card.card.id }

        cardToLearningBoxRepository.insertCardsForBox(cardIds=selectedCardsIds, learningBoxId = learningBoxId,
        onSuccess = { isFinished.value = true },
        onFailure = { error.value = "Whoops" })
    }
}