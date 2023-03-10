package de.fantjastisch.cards_frontend.glossary

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.update_and_create.ErrorHandlingViewModel
import de.fantjastisch.cards_frontend.util.fold
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.openapitools.client.models.CardEntity
import java.util.*


/**
 * Globales Objekt, welches die aktuellen Filteroptionen speichert
 */
object CardsFilters {
    private val initialFilters = CardFilters(
        search = "",
        tag = "",
        categories = emptyList(),
        sort = true
    )
    val filters = MutableStateFlow(initialFilters)

    fun reset() {
        filters.value = initialFilters
    }

    val hasFilters: Flow<Boolean> =
        filters.map { it.tag.isNotBlank() || it.search.isNotBlank() || it.categories.isNotEmpty() }
}

/**
 * Hält die Informationen über gewünschtes Filtern.
 *
 * @property search Zu suchender Begriff.
 * @property tag Zu suchendes Schlagwort.
 * @property categories Zu filternde Kategorien.
 * @property sort Wenn true alphabetisch sortiert.
 */
data class CardFilters(
    val search: String,
    val tag: String,
    val categories: List<UUID>,
    val sort: Boolean
)

/**
 * Stellt die Daten für die [GlossaryView] bereit und nimmt seine Anfragen entgegen.
 *
 * @property glossaryModel Das zugehörige Model, welches die Logik kapselt.
 *
 * @author Freja Sender, Tamari Bayer, Jessica Repty, Semjon Nirmann
 */
class GlossaryViewModel(
    private val glossaryModel: GlossaryModel = GlossaryModel()
) : ErrorHandlingViewModel() {

    val cards = mutableStateOf<List<CardEntity>>(emptyList())
    val currentDeleteDialog = mutableStateOf<DeletionProgress?>(null)

    init {
        viewModelScope.launch {
            CardsFilters.filters.collectLatest {
                onPageLoaded()
            }
        }
    }

    /**
     * Holt alle Karten aus der Datenbank, indem [GlossaryModel] angefragt wird.
     *
     */
    fun onPageLoaded() {
        viewModelScope.launch {
            glossaryModel.getCards()
                .fold(
                    onSuccess = { cards.value = it }
                )
        }
    }

    /**
     * Öffnet einen Dialog zum Bestätigen der Eingabe, falls eine Karte entfernt werden soll
     *
     * @param card die Karte, die gelöscht werden soll
     */
    fun onTryDeleteCard(card: CardEntity) {
        currentDeleteDialog.value = DeletionProgress.ConfirmWithUser(card)
    }

    /**
     * Das Entfernen von der Karte wurde bestätigt -> die Anfrage wird an [glossaryModel] weitergeleitet
     *
     */
    fun onDeleteCardClicked() {
        val card = currentDeleteDialog.value!!.card
        currentDeleteDialog.value = DeletionProgress.Deleting(card)
        viewModelScope.launch {
            glossaryModel.deleteCard(
                cardId = card.id
            ).fold(
                onSuccess = {
                    onPageLoaded()
                    currentDeleteDialog.value = null
                }
            )
        }
    }

    /**
     * Karte Löschvorgang wurde durch Nutzer abgebrochen. Dialogfenster schließen.
     *
     */
    fun onDeleteCardAborted() {
        currentDeleteDialog.value = null
    }


    /**
     * Dialog Fenster für das Bestätigen des Löschens einer Karte.
     *
     */
    sealed class DeletionProgress {
        abstract val card: CardEntity

        data class ConfirmWithUser(override val card: CardEntity) : DeletionProgress()
        data class Deleting(override val card: CardEntity) : DeletionProgress()
    }


}