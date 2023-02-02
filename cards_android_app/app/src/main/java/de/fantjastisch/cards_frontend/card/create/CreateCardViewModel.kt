package de.fantjastisch.cards_frontend.card.create

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.card.update_and_create.CreateLinkViewModel
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import kotlinx.coroutines.launch
import org.openapitools.client.models.LinkEntity
import java.util.*

/**
 * Stellt die Daten für die [CreateCardView] bereit und nimmt seine Anfragen entgegen.
 *
 * @property createCardModel Das zugehörige Model, welches die Logik kapselt.
 *
 * @author Tamari Bayer, Freja Sender, Jessica Repty, Semjon Nirmann
 */
class CreateCardViewModel(
    private val createCardModel: CreateCardModel = CreateCardModel()
) : CreateLinkViewModel() {

    // states, die vom View gelesen werden können -> automatisches Update vom View.
    val card = mutableStateOf(listOf<CardSelectItem>())

    // states, die vom view gelesen werden können -> automatisches Update vom View.
    val cardId = mutableStateOf<UUID?>(null)
    val isFinished = mutableStateOf(false)

    val cardQuestion = mutableStateOf("")
    val cardAnswer = mutableStateOf("")
    val cardTag = mutableStateOf("")
    val cardCategories = mutableStateOf(listOf<CategorySelectItem>())
    private val link = mutableStateOf<LinkEntity?>(null)
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

    fun onDeleteLinkClicked(link: LinkEntity) {
        cardLinks.value = cardLinks.value.filter { l -> link != l } as ArrayList<LinkEntity>
    }

    fun onCreateCardClicked() {
        error.value = null
        errors.value = emptyList()

        // check if fields for links have been filled but not saved
        if (linkName.value.isNotBlank() && linkTarget.value != null) {
            cardLinks.value = cardLinks.value +
                    LinkEntity(
                        label = linkName.value,
                        target = linkTarget.value!!
                    )
        }

        // check for categories -> if no then wait till yes
        if (cardCategories.value.none { cat -> cat.isChecked }) {
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