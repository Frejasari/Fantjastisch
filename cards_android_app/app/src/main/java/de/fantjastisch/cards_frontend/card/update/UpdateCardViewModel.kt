package de.fantjastisch.cards_frontend.card.update

import android.annotation.SuppressLint
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.infrastructure.fold
import kotlinx.coroutines.launch
import org.openapitools.client.models.ErrorEntryEntity
import org.openapitools.client.models.LinkEntity
import java.util.*

class UpdateCardViewModel(
    id: UUID,
    private val cardModel: UpdateCardModel = UpdateCardModel(id = id)
) : ViewModel() {

    val errors = mutableStateOf<List<ErrorEntryEntity>>(emptyList())
    val error = mutableStateOf<String?>(null)
    val isFinished = mutableStateOf(false)

    val cardQuestion = mutableStateOf("")
    val cardAnswer = mutableStateOf("")
    val cardTag = mutableStateOf("")
    val cardCategories = mutableStateOf(listOf<CategorySelectItem>())
    val cards = mutableStateOf(listOf<CardSelectItem>())
    val linkName = mutableStateOf("")
    val linkTarget = mutableStateOf<UUID?>(null)
    val link = mutableStateOf<LinkEntity?>(null)
    @SuppressLint("MutableCollectionMutableState")
    val cardLinks = mutableStateOf(ArrayList<LinkEntity>())
    val toast = mutableStateOf(false)
    val noCategories = mutableStateOf(false)

    fun setCardQuestion(value: String) {
        cardQuestion.value = value
    }

    fun setCardAnswer(value: String) {
        cardAnswer.value = value
    }

    fun setCardTag(value: String) {
        cardTag.value = value
    }

    fun setLinkName(value: String) {
        toast.value = false
        linkName.value = value
    }

    fun onCardSelected(id: UUID) {
        toast.value = false
        val selectedCards = cards.value.filter { card -> card.isChecked }

        if (selectedCards.isNotEmpty()) {
            cards.value = cards.value.map {
                it.copy(isChecked = false)
            }
        }
        cards.value = cards.value.map {
            if (it.card.id == id) {
                it.copy(isChecked = !it.isChecked)
            } else {
                it
            }
        }

        val list = cards.value
            .filter{ card -> card.isChecked }
        linkTarget.value = list[0].card.id
    }

    init {
        viewModelScope.launch {
            cardModel
                .initializePage()
                .fold(
                    onSuccess = { card ->
                        errors.value = emptyList()
                        cardAnswer.value = card.answer
                        cardQuestion.value = card.question
                        cardTag.value = card.tag
                        cardCategories.value = card.allCategories.map { cat ->
                            CategorySelectItem(
                                id = cat.id,
                                label = cat.label,
                                isChecked = card.categoriesOfCard.firstOrNull { categoryOfCard -> categoryOfCard.id == cat.id } != null
                            )
                        }
                        cardLinks.value = card.links as ArrayList<LinkEntity>
                    },
                    onValidationError = { error.value = "Fehler bei der Eingabevalidierung." },
                    onUnexpectedError = { error.value = "Ein unbekannter Fehler ist aufgetreten." },
                )

            val resultCards = cardModel.getCards()

            if (resultCards == null) {
                error.value = "Ein Netzwerkfehler ist aufgetreten."
            } else {
                errors.value = emptyList()
                cards.value = resultCards
            }
        }
    }

    fun onCreateLinkClicked() {
        error.value = null
        errors.value = emptyList()

        if (linkName.value.isBlank() || linkTarget.value == null) {
            toast.value = true
        } else {
            toast.value = false
            link.value = LinkEntity(
                label = linkName.value,
                target = linkTarget.value!!
            )
            cardLinks.value.add(link.value!!)

            linkName.value = ""
            cards.value = cards.value.map {
                it.copy(isChecked = false)
            }
        }
    }

    fun onDeleteLinkClicked(link: LinkEntity) {
        cardLinks.value = cardLinks.value.filter { l -> link != l } as ArrayList<LinkEntity>
    }


    fun onUpdateCardClicked() {
        errors.value = emptyList()

        if(linkName.value.isNotBlank() && linkTarget.value != null) {
            cardLinks.value.add(LinkEntity(
                label = linkName.value,
                target = linkTarget.value!!
            ))
        }

        if (cardCategories.value.none { cat -> cat.isChecked }) {
            noCategories.value = true
        } else {
            viewModelScope.launch {
                cardModel.update(
                    question = cardQuestion.value,
                    answer = cardAnswer.value,
                    tag = cardTag.value,
                    categories = cardCategories.value,
                    links = cardLinks.value
                ).fold(
                    onSuccess = { isFinished.value = true },
                    onValidationError = { errors.value = it },
                    onUnexpectedError = { error.value = "Ein unbekannter Fehler ist aufgetreten." }
                )
            }
        }
    }


    fun onCategorySelected(id: UUID) {
        cardCategories.value = cardCategories.value.map {
            if (it.id == id) {
                it.copy(isChecked = !it.isChecked)
            } else {
                it
            }
        }
        noCategories.value = false
    }


}

