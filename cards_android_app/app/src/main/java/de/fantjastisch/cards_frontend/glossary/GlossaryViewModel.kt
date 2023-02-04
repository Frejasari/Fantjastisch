package de.fantjastisch.cards_frontend.glossary

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.update_and_create.ErrorHandlingViewModel
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.openapitools.client.models.CardEntity
import java.util.*


/**
 * Globales Objekt, welches die aktuellen Filteroptionen speichert
 */
object CardsFilters {
    val filters = MutableStateFlow(
        CardFilters(
            search = "",
            tag = "",
            categories = emptyList(),
            sort = false
        )
    )
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
            val result = glossaryModel.getCards(
                categoryIds = CardsFilters.filters.value.categories,
                search = CardsFilters.filters.value.search,
                tag = CardsFilters.filters.value.tag,
                sort = CardsFilters.filters.value.sort
            )
            when (result) {
                is RepoResult.Success -> {
                    cards.value = result.result
                    Log.v("CardsFilter", "Received ${result.result.size} cards")
                }
                is RepoResult.Error -> setValidationErrors(result.errors)
                is RepoResult.ServerError -> setUnexpectedErrors()

            }
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
            val result = glossaryModel.deleteCard(
                cardId = card.id
            )
            when (result) {
                is RepoResult.Success -> {
                    onPageLoaded()
                    currentDeleteDialog.value = null
                }
                is RepoResult.Error -> setValidationErrors(result.errors)
                is RepoResult.ServerError -> setUnexpectedErrors()
            }
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