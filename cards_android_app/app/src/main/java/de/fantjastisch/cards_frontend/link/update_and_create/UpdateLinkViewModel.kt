package de.fantjastisch.cards_frontend.link.update_and_create

import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.card.update_and_create.UpdateAndCreateCardViewModel
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.category.CategoryViewModel
import de.fantjastisch.cards_frontend.link.LinkRepository
import de.fantjastisch.cards_frontend.link.LinkViewModel
import org.openapitools.client.models.CardEntity
import org.openapitools.client.models.UpdateCardEntity
import org.openapitools.client.models.UpdateCategoryEntity
import org.openapitools.client.models.UpdateLinkEntity
import java.util.*

class UpdateLinkViewModel(
    val link: UUID,
    val card: UUID,
    private val linkRepository: LinkRepository = LinkRepository(),
    private val cardRepository: CardRepository = CardRepository()
) : UpdateAndCreateLinkViewModel(
    linkId = link,
    cardId = card,
    cardRepository = cardRepository,
    linkRepository = linkRepository
) {

    private lateinit var targetOfLink: UUID

    init {
        linkRepository.getLink(id = link,
            onSuccess = {
                errors.value = emptyList()
                linkId = it.id
                linkName.value = it.name!!
                linkSource = cardId
                targetOfLink = it.target!!
                loadAllCards()
            },
            onFailure = {
                error.value = "Check network connection"
            })
    }

    private fun loadAllCards() {
        cardRepository.getPage(null,null,null,false,
        onSuccess = {
            errors.value = emptyList()
            val newCards = it
                .filter { card -> !id.equals(linkSource) }
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
        errors.value = emptyList()
        linkRepository.updateLink(
            link = UpdateLinkEntity(
                id = link,
                name = linkName.value,
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
    }
}

