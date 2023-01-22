package de.fantjastisch.cards_frontend.link.update_and_create

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.category.CategoryRepository
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.link.LinkRepository
import org.openapitools.client.models.CardEntity
import org.openapitools.client.models.ErrorEntryEntity
import java.util.*
import java.util.Locale.Category

abstract class UpdateAndCreateLinkViewModel(
    var linkId: UUID? = null,
    val cardId: UUID? = null,
    private val cardRepository: CardRepository,
    private val linkRepository: LinkRepository
) : ViewModel() {

    // states, die vom view gelesen werden können -> automatisches Update vom View.
    val errors = mutableStateOf<List<ErrorEntryEntity>>(emptyList())
    val error = mutableStateOf<String?>(null)
    val isFinished = mutableStateOf(false)

    val id = mutableStateOf<UUID?>(null)
    val linkName = mutableStateOf("")
    var linkSource = cardId
    val linkTarget = mutableStateOf<UUID?>(null)
    val allCards = mutableStateOf(listOf<CardSelectItem>())
/*
    init {
        if(linkId != null) {
            id.value = linkId
        }
        cardRepository.getPage(null,null,null,false,
        onSuccess = {
            errors.value = emptyList()

            if(allCards.value.isEmpty()) {
                allCards.value = it
                    .filter { card ->
                        card.id != cardId
                    }
                    .map { card ->
                    CardSelectItem(
                        card = card,
                        isChecked = false
                    )
                }
            } else {
                val targetOfLink = allCards.value
                val newCards = it.map { card ->
                    CardSelectItem(
                        card = card,
                        isChecked = targetOfLink
                            .firstOrNull() { target -> target.card.id == card.id } != null
                    )
                }
                allCards.value = newCards
            }
            if(allCards.value.size > 1) {
                error.value = "Pick one card"
            } else {
                linkTarget.value = allCards.value[0].card.id
            }

        },
        onFailure = {
            error.value = "Check network connection"
        })
    } */

    fun onCardSelected(id: UUID) {
        allCards.value = allCards.value.map {
            if (it.card.id == id) {
                it.copy(isChecked = !it.isChecked)
            } else {
                it
            }
        }
    }

    abstract fun save()
}
