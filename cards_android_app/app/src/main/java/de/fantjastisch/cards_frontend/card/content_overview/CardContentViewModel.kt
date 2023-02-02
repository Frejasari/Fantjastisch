package de.fantjastisch.cards_frontend.card.content_overview

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.infrastructure.fold
import kotlinx.coroutines.launch
import org.openapitools.client.models.CardEntity
import java.util.*

/**
 * Zeigt eine ausgeklappte Karte im Glossar an.
 *
 * @author Tamari Bayer, Freja Sender, Jessica Repty, Semjon Nirmann
 * **/
class CardContentViewModel(
    private val id: UUID,
    private val cardContentModel: CardContentModel = CardContentModel(id = id)
) : ViewModel() {
    val card = mutableStateOf<CardEntity?>(null)

    val error = mutableStateOf<String?>(null)
    val isFinished = mutableStateOf(false)

    init {
        viewModelScope.launch {
            cardContentModel
                .initializePage()
                .fold(
                    onSuccess = { cardResponse ->
                        card.value = cardResponse
                    },
                    onValidationError = { error.value = "Fehler bei der Eingabevalidierung." },
                    onUnexpectedError = { error.value = "Ein unbekannter Fehler ist aufgetreten." },
                )
        }
    }
}

