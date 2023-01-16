package de.fantjastisch.cards_frontend.card

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
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