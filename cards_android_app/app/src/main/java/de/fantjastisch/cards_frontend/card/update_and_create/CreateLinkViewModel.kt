package de.fantjastisch.cards_frontend.card.update_and_create

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.card.update.UpdateCardView
import org.openapitools.client.models.ErrorEntryEntity
import org.openapitools.client.models.LinkEntity
import java.util.*

/**
 * Stellt die Daten für die [] bereit und nimmt seine Anfragen entgegen.
 *
 * @author Freja Sender
 */
open class CreateLinkViewModel : ViewModel() {

    val linkName = mutableStateOf("")
    val linkTarget = mutableStateOf<UUID?>(null)
    private val link = mutableStateOf<LinkEntity?>(null)
    val toast = mutableStateOf(false)
    val cards = mutableStateOf(listOf<CardSelectItem>())
    val error = mutableStateOf<String?>(null)
    val errors = mutableStateOf<List<ErrorEntryEntity>>(emptyList())

    val cardLinks = mutableStateOf<List<LinkEntity>>(listOf())

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
            .filter { card -> card.isChecked }
        linkTarget.value = list[0].card.id
    }


    fun onCreateLinkClicked() {
        error.value = null
        errors.value = emptyList()

        if (linkName.value.isBlank()) {
            errors.value = listOf(
                ErrorEntryEntity(
                    code = ErrorEntryEntity.Code.nOTBLANKVIOLATION,
                    message = "Darf nicht leer sein",
                    field = "linkName"
                )
            )
        } else if (linkTarget.value == null) {
            toast.value = true
        } else {
            toast.value = false
            link.value = LinkEntity(
                label = linkName.value,
                target = linkTarget.value!!
            )
            cardLinks.value = cardLinks.value + link.value!!


            linkName.value = ""
            cards.value = cards.value.map {
                it.copy(isChecked = false)
            }
        }
    }
}
