package de.fantjastisch.cards_frontend.card.content_overview

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.infrastructure.fold
import de.fantjastisch.cards_frontend.link.LinkRepository
import kotlinx.coroutines.launch
import org.openapitools.client.models.ErrorEntryEntity
import org.openapitools.client.models.LinkEntity
import java.util.*

class CardContentViewModel(
    id: UUID,
    private val cardContentModel: CardContentModel = CardContentModel(id = id)
) : ViewModel() {
    val errors = mutableStateOf<List<ErrorEntryEntity>>(emptyList())
    val error = mutableStateOf<String?>(null)
    val isFinished = mutableStateOf(false)

    val cardId = mutableStateOf<UUID?>(null)
    val cardQuestion = mutableStateOf("")
    val cardAnswer = mutableStateOf("")
    val cardTag = mutableStateOf("")
    val cardCategories = mutableStateOf(listOf<CategorySelectItem>())
    val cardLinks = mutableStateOf(listOf<LinkEntity>())

    init {
        viewModelScope.launch {
            cardContentModel
                .initializePage()
                .fold(
                    onSuccess = { card ->
                        errors.value = emptyList()
                        cardId.value = card.id
                        cardAnswer.value = card.answer
                        cardQuestion.value = card.question
                        cardTag.value = card.tag
                        cardCategories.value = card.categoriesOfCard.map { category ->
                            CategorySelectItem(
                                label = category.label,
                                id = category.id,
                                isChecked = true
                            )
                        }
                        cardLinks.value = card.links
                    },
                    onError = { error.value = "Something is wrong" },
                    onUnexpectedError = { error.value = "Irgendwas ist schief gelaufen" },
                )
        }
    }
}

