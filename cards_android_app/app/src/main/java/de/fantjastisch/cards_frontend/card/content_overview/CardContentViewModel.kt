package de.fantjastisch.cards_frontend.card.content_overview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.glossary.GlossaryViewModel
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import de.fantjastisch.cards_frontend.infrastructure.fold
import kotlinx.coroutines.launch
import org.openapitools.client.models.CardEntity
import org.openapitools.client.models.ErrorEntryEntity
import org.openapitools.client.models.LinkEntity
import java.util.*

class CardContentViewModel(
    id: UUID,
    private val cardContentModel: CardContentModel = CardContentModel(id = id)
) : ViewModel() {
    val card = mutableStateOf<CardEntity?>(null)

    val errors = mutableStateOf<List<ErrorEntryEntity>>(emptyList())
    val error = mutableStateOf<String?>(null)
    val isFinished = mutableStateOf(false)

    val cardId = mutableStateOf<UUID?>(null)
    val cardQuestion = mutableStateOf("")
    val cardAnswer = mutableStateOf("")
    val cardTag = mutableStateOf("")
    val cardCategories = mutableStateOf(listOf<CategorySelectItem>())
    val cardLinks = mutableStateOf(listOf<LinkEntity>())

    var linkClicked =  mutableStateOf(false)

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
                    onValidationError = { error.value = "Fehler bei der Eingabevalidierung." },
                    onUnexpectedError = { error.value = "Ein unbekannter Fehler ist aufgetreten." },
                )
        }
    }

    fun onPageLoaded() {
        viewModelScope.launch {
            val result = cardContentModel.getCard(id = cardId.value!!)
            when (result) {
                is RepoResult.Success -> {
                    card.value = result.result
                }
                is RepoResult.Error -> Unit
                is RepoResult.ServerError -> Unit

            }
        }
    }

    fun onLinkClicked() {
        linkClicked.value = true
    }

    fun onQuitLinkClicked() {
        linkClicked.value = false
    }
}

