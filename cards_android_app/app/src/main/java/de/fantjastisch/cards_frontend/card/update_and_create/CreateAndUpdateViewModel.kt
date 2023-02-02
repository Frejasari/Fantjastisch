package de.fantjastisch.cards_frontend.card.update_and_create

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.util.ErrorsEnum
import org.openapitools.client.models.ErrorEntryEntity
import org.openapitools.client.models.LinkEntity
import java.util.*

/**
 * Parent Class, die gemeinsame Daten  & Funktionen zum Erzeugen und Bearbeiten von Karten bereitstellt
 *
 * @author Freja Sender
 */
open class CreateAndUpdateViewModel : ViewModel() {

    val linkLabel = mutableStateOf("")
    val error = mutableStateOf(ErrorsEnum.NO_ERROR)
    val cards = mutableStateOf(listOf<CardSelectItem>())
    val errors = mutableStateOf<List<ErrorEntryEntity>>(emptyList())

    val cardLinks = mutableStateOf<List<LinkEntity>>(listOf())

    fun setLinkName(value: String) {
        linkLabel.value = value
    }

    fun onCardSelected(id: UUID) {
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

    }

    fun onDeleteLinkClicked(link: LinkEntity) {
        cardLinks.value = cardLinks.value.filter { l -> link != l } as ArrayList<LinkEntity>
    }

    fun onToastShown() {
        error.value = ErrorsEnum.NO_ERROR
    }

    fun onCreateLinkClicked() {
        errors.value = emptyList()

        val target = cards.value.firstOrNull() { card -> card.isChecked }

        if (linkLabel.value.isBlank()) {
            errors.value = errors.value +
                    ErrorEntryEntity(
                        code = ErrorEntryEntity.Code.nOTBLANKVIOLATION,
                        message = "Darf nicht leer sein",
                        field = "linkName"
                    )
        } else if (target == null) {
            error.value = ErrorsEnum.LINK_ERROR
        } else {
            cardLinks.value = cardLinks.value + LinkEntity(
                label = linkLabel.value,
                target = target.card.id
            )
            linkLabel.value = ""

            cards.value = cards.value.map {
                it.copy(isChecked = false)
            }
        }
    }
}
