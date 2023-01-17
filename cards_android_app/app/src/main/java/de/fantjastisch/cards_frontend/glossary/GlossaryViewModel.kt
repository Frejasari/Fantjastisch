package de.fantjastisch.cards_frontend.glossary

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import de.fantjastisch.cards_frontend.card.CardRepository
import org.openapitools.client.models.CardEntity

class GlossaryViewModel(
    private val cardRepository: CardRepository = CardRepository()
) : ViewModel() {

    val cards = mutableStateOf<List<CardEntity>>(emptyList())

    fun onPageLoaded() {
        cardRepository.getPage(
            categoryIds = null,
            search = null,
            tag = null,
            sort = null,
            onSuccess = { cards.value = it },
            onFailure = {}
        )
    }

    init {
        onPageLoaded()
    }
}