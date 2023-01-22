package de.fantjastisch.cards_frontend.link

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import de.fantjastisch.cards_frontend.card.CardRepository
import org.openapitools.client.models.ErrorEntryEntity
import org.openapitools.client.models.LinkEntity
import java.util.*

open class LinkViewModel(
    private val id: UUID? = null,
    private val cardId: UUID? = null,
    private val linkRepository: LinkRepository = LinkRepository()
) : ViewModel() {

    // states, die vom view gelesen werden können -> automatisches Update vom View.
    val errors = mutableStateOf<List<ErrorEntryEntity>>(emptyList())
    val error = mutableStateOf<String?>(null)
    val isFinished = mutableStateOf(false)

    val linkId = mutableStateOf<UUID?>(null)
    val linkName = mutableStateOf("")
    val source = mutableStateOf<UUID?>(null)
    val target = mutableStateOf<UUID?>(null)
    val linksOfCard = mutableStateOf<List<LinkEntity>>(emptyList())

    init {
        linkRepository.getLinkPage(cardId!!,
            onSuccess = {
                errors.value = emptyList()
                linksOfCard.value  = it

            },
            onFailure = {
                error.value = "Check network connection"
            }

        )
    }
/*
    fun onCategorySelected(id: UUID) {
        subcategories.value = subcategories.value.map {
            if (it.id == id) {
                it.copy(isChecked = !it.isChecked)
            } else {
                it
            }
        }
    }*/
}