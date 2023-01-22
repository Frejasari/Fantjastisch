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
) : ViewModel() {

    // states, die vom view gelesen werden können -> automatisches Update vom View.
    val errors = mutableStateOf<List<ErrorEntryEntity>>(emptyList())
    val error = mutableStateOf<String?>(null)
    val isFinished = mutableStateOf(false)

    val id = linkId
    var linkSource = mutableStateOf<UUID?>(null)
    val linkName = mutableStateOf("")
    val allCards = mutableStateOf(listOf<CardSelectItem>())


    init {
        if(cardId != null) {
            linkSource.value = cardId
        }
    }




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
