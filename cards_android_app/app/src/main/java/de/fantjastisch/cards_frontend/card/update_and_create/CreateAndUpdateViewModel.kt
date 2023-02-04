package de.fantjastisch.cards_frontend.card.update_and_create

import androidx.compose.runtime.mutableStateOf
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.util.ErrorsEnum
import org.openapitools.client.models.ErrorEntryEntity
import org.openapitools.client.models.LinkEntity
import java.util.*

/**
 * Parent Class, die gemeinsame Daten & Funktionen zum Erzeugen und Bearbeiten von Karten bereitstellt.
 *
 * @author Freja Sender
 */
open class CreateAndUpdateViewModel : ErrorHandlingViewModel() {

    val linkLabel = mutableStateOf("")
    val cards = mutableStateOf(listOf<CardSelectItem>())
    val categories = mutableStateOf(listOf<CategorySelectItem>())

    val cardLinks = mutableStateOf<List<LinkEntity>>(listOf())
    val cardQuestion = mutableStateOf("")
    val cardAnswer = mutableStateOf("")
    val cardTag = mutableStateOf("")


    /**
     * Speichert das übergebene Linklabel in [linkLabel].
     *
     * @param value Label des Links.
     */
    fun setLinkName(value: String) {
        linkLabel.value = value
    }


    /**
     * Setzt die Frage der Karte auf den übergebenen Wert.
     *
     * @param value Neue Frage der Karte.
     */
    fun setCardQuestion(value: String) {
        cardQuestion.value = value
    }

    /**
     * Setzt die Antwort der Karte auf den übergebenen Wert.
     *
     * @param value Neue Antwort der Karte.
     */
    fun setCardAnswer(value: String) {
        cardAnswer.value = value
    }

    /**
     * Setzt das Schlagwort der Karte auf den übergebenen Wert.
     *
     * @param value Neues Schlagwort der Karte.
     */
    fun setCardTag(value: String) {
        cardTag.value = value
    }

    /**
     * Speichert für die ausgewählten Karten isChecked = true in [cards].
     *
     * @param id Id, der Karte die ausgewählt wurde.
     */
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

    /**
     * Speichert die ausgewählten Kategorien als isChecked = true in [categories].
     *
     * @param id Id der Kategorie, welche neu ausgewählt wurde.
     */
    fun onCategorySelected(id: UUID) {
        categories.value = categories.value.map {
            if (it.id == id) {
                it.copy(isChecked = !it.isChecked)
            } else {
                it
            }
        }
    }

    /**
     * Löscht den Link, welcher in der Bearbeiten/Erstellen View erzeugt wurde und
     * welcher zum Löschen angeklickt wurde.
     *
     * @param link Link, welcher gelöscht wird.
     */
    fun onDeleteLinkClicked(link: LinkEntity) {
        cardLinks.value = cardLinks.value.filter { l -> link != l } as ArrayList<LinkEntity>
    }

    /**
     * Wenn Link gespeichert wird -> erstellt den Link mit den in [linkLabel]
     * gespeicherten Label und dem in [cards] gespeicherten Target.
     *
     */
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
