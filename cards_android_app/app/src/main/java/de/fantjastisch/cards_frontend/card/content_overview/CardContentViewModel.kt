package de.fantjastisch.cards_frontend.card.content_overview

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.category.CategorySelectItem
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

    val currentDeleteDialog = mutableStateOf<CardContentViewModel.DeletionProgress?>(null)

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
                    onValidationError = { error.value = "Something is wrong" },
                    onUnexpectedError = { error.value = "Irgendwas ist schief gelaufen" },
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

    fun onTryDeleteLink(link: LinkEntity) {
        currentDeleteDialog.value = DeletionProgress.ConfirmWithUser(link)
    }

    fun onDeleteCardClicked() {
        val link = currentDeleteDialog.value!!.link
        currentDeleteDialog.value = DeletionProgress.Deleting(link)
        error.value = null
        viewModelScope.launch {
            val result = cardContentModel.deleteLink(
                linkId = link.id!!
            )
            when (result) {
                is RepoResult.Success -> {
                    onPageLoaded()
                    currentDeleteDialog.value = null
                }
                is RepoResult.Error,
                is RepoResult.ServerError -> {
                    // Fehler anzeigen:
                    error.value = "Irgendwas ist schief gelaufen"
                }

            }
        }
    }

    fun onDeleteCardAborted() {
        currentDeleteDialog.value = null
    }

    sealed class DeletionProgress {
        abstract val link: LinkEntity

        data class ConfirmWithUser(override val link: LinkEntity) : DeletionProgress()
        data class Deleting(override val link: LinkEntity) : DeletionProgress()
    }
}

