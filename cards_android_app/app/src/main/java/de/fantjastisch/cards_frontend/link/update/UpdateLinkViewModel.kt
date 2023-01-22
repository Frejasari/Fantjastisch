package de.fantjastisch.cards_frontend.link.update_and_create

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.card.update.UpdateCardModel
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.category.CategoryViewModel
import de.fantjastisch.cards_frontend.infrastructure.fold
import de.fantjastisch.cards_frontend.link.LinkRepository
import de.fantjastisch.cards_frontend.link.update.UpdateLinkModel
import kotlinx.coroutines.launch
import org.openapitools.client.models.*
import java.util.*

class UpdateLinkViewModel(
    linkId: UUID,
    sourceId: UUID,
    private val linkModel: UpdateLinkModel = UpdateLinkModel(linkId = linkId, sourceId = sourceId)
) : ViewModel() {
    val errors = mutableStateOf<List<ErrorEntryEntity>>(emptyList())
    val error = mutableStateOf<String?>(null)
    val isFinished = mutableStateOf(false)

    val linkName = mutableStateOf("")
    val linkSource = mutableStateOf("")
    val linkTarget = mutableStateOf<UUID?>(null)
    val linkCards = mutableStateOf(listOf<CardSelectItem>())

    fun setLinkName(value: String) {
        linkName.value = value
    }

    init {
        viewModelScope.launch {
            linkModel
                .initializePage()
                .fold(
                    onSuccess = { link ->
                        errors.value = emptyList()
                        linkName.value = link.name
                      //  linkTarget.value = link.targetId
                    },
                    onError = { error.value = "Something is wrong" },
                    onUnexpectedError = { error.value = "Irgendwas ist schief gelaufen" },
                )
        }
    }

    fun onUpdateLinkClicked() {
        errors.value = emptyList()
        viewModelScope.launch {
            linkModel.update(
                name = linkName.value,
                targetId = linkCards.value[0].card.id,
            ).fold(
                onSuccess = { isFinished.value = true },
                onError = { errors.value = it },
                onUnexpectedError = { error.value = "Irgendwas ist schief gelaufen" }
            )
        }
    }


    fun onCardSelected(id: UUID) {
        linkCards.value = linkCards.value.map {
            if (it.card.id == id) {
                it.copy(isChecked = !it.isChecked)
            } else {
                it
            }
        }
    }


}


/*
class UpdateLinkViewModel(
    val link: UUID,
    val card: UUID,
    private val linkRepository: LinkRepository = LinkRepository(),
    private val cardRepository: CardRepository = CardRepository()
) : UpdateAndCreateLinkViewModel(
    linkId = link,
    cardId = card,
) {

    private lateinit var targetOfLink: UUID

    init {
        linkRepository.getLink(id = link,
            onSuccess = {
                errors.value = emptyList()
                linkId = it.id
                linkName.value = it.name!!
                linkSource.value = cardId
                targetOfLink = it.target!!
                loadAllCards()
            },
            onFailure = {
                error.value = "Check network connection"
            })
    }

    private fun loadAllCards() {
        cardRepository.getPage(null, null, null, false,
            onSuccess = {
                errors.value = emptyList()
                val newCards = it
                    .filter { card -> card.id != linkSource.value }
                    .map { card ->
                        CardSelectItem(
                            card = CardEntity(
                                id = card.id,
                                question = card.question,
                                answer = card.answer,
                                tag = card.tag,
                                categories = card.categories
                            ),
                            isChecked = targetOfLink == card.id
                        )
                    }
                allCards.value = newCards
            },
            onFailure = {
                error.value = "Check network connection"
            })
    }

    override fun save() {
        val target = mutableStateOf<UUID?>(null)

        errors.value = emptyList()
        val toUpdate: UpdateLinkEntity

        allCards.value
            .filter { it.isChecked }
            .let {
                target.value = it[0].card.id
                toUpdate = UpdateLinkEntity(
                    id = link,
                    name = linkName.value,
                    source = linkSource.value!!,
                    target = target.value!!)
            }
            .let {
                linkRepository.updateLink(
                    link = toUpdate,
                    onSuccess = { isFinished.value = true },
                    onFailure = {
                        if (it == null) {
                            // Fehler anzeigen:
                            error.value = "Irgendwas ist schief gelaufen"
                        } else {
                            errors.value = it.errors
                        }
                    }
                )
            }
        /*
        errors.value = emptyList()
        linkRepository.updateLink(
            link = UpdateLinkEntity(
                id = link,
                name = "name",//linkName.value,
                source = linkSource!!,
                target = allCards.value.filter { it.isChecked }.map { it.card.id }[0],
            ),
            onSuccess = {
                isFinished.value = true

                // on Success -> dialog schliessen, zur Card  übersicht?
            },
            onFailure = {
                if (it == null) {
                    error.value = "Irgendwas ist schief gelaufen"
                } else {
                    errors.value = it.errors
                }
                // Fehler anzeigen:
                error.value = "There is an error"
            }
        )
    } */
    }
} */

