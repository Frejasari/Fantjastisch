package de.fantjastisch.cards_frontend.card.update

import android.annotation.SuppressLint
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.glossary.GlossaryViewModel
import de.fantjastisch.cards_frontend.infrastructure.fold
import kotlinx.coroutines.launch
import org.openapitools.client.models.CardEntity
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
    var linkClicked =  mutableStateOf(false)

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
        linkName.value = value
    }

    //TODO -> nur ein checkbox erlauben
    fun onCardSelected(id: UUID) {
        cards.value = cards.value.map {
            if (it.card.id == id) {
                it.copy(isChecked = !it.isChecked)
            } else {
                it
            }
        }

        linkTarget.value = cards.value.map {
            if (it.card.id == id) {
                it.copy(isChecked = !it.isChecked)
            } else {
                it
            }
        }.get(0).card.id
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

        link.value = LinkEntity(
            label = linkName.value,
            target = linkTarget.value
        )
        cardLinks.value.add(link.value!!)

        linkName.value = ""
        cards.value = cards.value.map {
            it.copy(isChecked = false)
        }

    }

    fun onDeleteLinkClicked(link: LinkEntity) {
        cardLinks.value = cardLinks.value.filter {
                l -> link != l} as ArrayList<LinkEntity>
    }

    //TODO für delete die linkentity einfach aus der liste rausnehmen
    fun onUpdateCardClicked() {
        errors.value = emptyList()
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


    fun onCategorySelected(id: UUID) {
        cardCategories.value = cardCategories.value.map {
            if (it.id == id) {
                it.copy(isChecked = !it.isChecked)
            } else {
                it
            }
        }
    }



}

