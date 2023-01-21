package de.fantjastisch.cards_frontend.learning_object_details.cards_view

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

class CardsInBoxViewModel(
    private val learningBoxId: UUID,
    private val cardRepository: CardRepository = CardRepository(),
    private val cardToLearningBoxRepository: CardToLearningBoxRepository = CardToLearningBoxRepository(
        InternalCardToLearningBoxRepository(AppDatabase.database.cardToLearningBoxDao())
    )
) : ViewModel() {

    val cards = mutableStateOf<List<CardEntity>>(emptyList())
    val cardsInBox = mutableStateOf<List<CardSelectItem>>(mutableListOf())
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
                cards.value = it
                getContainedCards()
            },
            onFailure = { error.value = "Couldnt fetch cards." }
        )


    }

    private fun getContainedCards() {
        cardToLearningBoxRepository.getCardIdsForBox(learningBoxId = learningBoxId,
            onSuccess = {
                val containedCards = cards.value.filter { card -> it.contains(card.id) }
                cardsInBox.value = containedCards.map { cardEntity ->
                    CardSelectItem(id = cardEntity.id,
                        question = cardEntity.question,
                        answer = cardEntity.answer,
                        tag = cardEntity.tag,
                        categories = cardEntity.categories.map { category -> category.label },
                        isChecked = false
                    )
                }
            },
            onFailure = {
                error.value = "Couldnt get card ids for box."
            })
    }
}