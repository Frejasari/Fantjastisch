package de.fantjastisch.cards_frontend.card.create

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import kotlinx.coroutines.launch
import org.openapitools.client.models.ErrorEntryEntity
import org.openapitools.client.models.LinkEntity
import java.util.*

class CreateCardViewModel(
    private val createCardModel: CreateCardModel = CreateCardModel()
) : ViewModel() {

    // states, die vom View gelesen werden können -> automatisches Update vom View.
    val card = mutableStateOf(listOf<CardSelectItem>())

    // states, die vom view gelesen werden können -> automatisches Update vom View.
    val cardId = mutableStateOf<UUID?>(null)
    val errors = mutableStateOf<List<ErrorEntryEntity>>(emptyList())
    val error = mutableStateOf<String?>(null)
    val isFinished = mutableStateOf(false)

    val cardQuestion = mutableStateOf("")
    val cardAnswer = mutableStateOf("")
    val cardTag = mutableStateOf("")
    val cardCategories = mutableStateOf(listOf<CategorySelectItem>())
    val cards = mutableStateOf(listOf<CardSelectItem>())
    val linkName = mutableStateOf("")
    private val linkTarget = mutableStateOf<UUID?>(null)
    private val link = mutableStateOf<LinkEntity?>(null)
    val cardLinks = mutableStateOf(mutableListOf<LinkEntity>())
    val toast = mutableStateOf(false)
    val noCategories = mutableStateOf(false)

    init {
        viewModelScope.launch {

            val result = createCardModel.getCategories()
            val resultCards = createCardModel.getCards()

            if (result == null || resultCards == null) {
                error.value = "Ein Netzwerkfehler ist aufgetreten."
            } else {
                errors.value = emptyList()
                cardCategories.value = result
                cards.value = resultCards
            }
        }
    }

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
        toast.value = false
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

    fun onCreateCardClicked() {
        error.value = null
        errors.value = emptyList()

        // check if fields for links have been filled but not saved
        if(linkName.value.isNotBlank() && linkTarget.value != null) {
            cardLinks.value.add(
                LinkEntity(
                label = linkName.value,
                target = linkTarget.value!!
            )
            )
        }

        // check for categories -> if no then wait till yes
        if(cardCategories.value.none { cat -> cat.isChecked }) {
            noCategories.value = true
        } else {
            viewModelScope.launch {
                val result = createCardModel.createCard(
                    question = cardQuestion.value,
                    answer = cardAnswer.value,
                    tag = cardTag.value.replaceFirstChar { letter -> letter.uppercaseChar() },
                    links = cardLinks.value,
                    categories = cardCategories.value
                )

                when (result) {
                    is RepoResult.Success -> isFinished.value = true
                    is RepoResult.Error -> errors.value = result.errors
                    is RepoResult.ServerError -> error.value = "Ein Netzwerkfehler ist aufgetreten."
                }
            }
        }


    }


}