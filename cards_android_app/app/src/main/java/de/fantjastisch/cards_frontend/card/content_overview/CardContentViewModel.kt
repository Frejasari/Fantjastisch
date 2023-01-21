package de.fantjastisch.cards_frontend.card.content_overview

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import de.fantjastisch.cards_frontend.card.CardRepository
import org.openapitools.client.models.ErrorEntryEntity
import java.util.*

class CardContentViewModel(
    id: UUID,
    private val cardRepository: CardRepository = CardRepository()
) : ViewModel() {
    val errors = mutableStateOf<List<ErrorEntryEntity>>(emptyList())
    val error = mutableStateOf<String?>(null)
    val isFinished = mutableStateOf(false)

    val cardId = mutableStateOf<UUID?>(null)
    val cardQuestion = mutableStateOf("")
    val cardAnswer = mutableStateOf("")
    val cardTag = mutableStateOf("")
    val cardCategories = mutableStateOf(listOf<String>())

    init {
        cardRepository.getCard(id = id,
            onSuccess = {
                errors.value = emptyList()
                cardId.value = it.id
                cardAnswer.value = it.answer
                cardQuestion.value = it.question
                cardTag.value = it.tag
                cardCategories.value = it.categories.map { cat -> cat.label}
            },
            onFailure = {
                error.value = "Check network connection"
            })
    }
}

