package de.fantjastisch.cards_frontend.card.content_overview

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.update_and_create.ErrorHandlingViewModel
import de.fantjastisch.cards_frontend.util.fold
import kotlinx.coroutines.launch
import org.openapitools.client.models.CardEntity
import java.util.*

/**
 * Stellt die Daten für die [CardContentDialogView] bereit und nimmt seine Anfragen entgegen.
 *
 * @property id Die Id von der ausgeklappten Karte.
 * @property cardContentModel Das zugehörige Model, welches die Logik kapselt.
 *
 * @author Tamari Bayer, Freja Sender, Jessica Repty, Semjon Nirmann
 */
class CardContentViewModel(
    private val id: UUID,
    private val cardContentModel: CardContentModel = CardContentModel(id = id)
) : ErrorHandlingViewModel() {
    val card = mutableStateOf<CardEntity?>(null)

    val isFinished = mutableStateOf(false)

    init {
        viewModelScope.launch {
            cardContentModel
                .initializePage()
                .fold(
                    onSuccess = { cardResponse ->
                        card.value = cardResponse
                    },
                    onValidationError = ::setValidationErrors,
                    onUnexpectedError = ::setUnexpectedError,
                )
        }
    }
}

