package de.fantjastisch.cards_frontend.link.update_and_create

import androidx.compose.runtime.mutableStateOf
import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.card.update_and_create.UpdateAndCreateCardViewModel
import de.fantjastisch.cards_frontend.link.LinkRepository
import de.fantjastisch.cards_frontend.link.LinkSelectItem
import de.fantjastisch.cards_frontend.link.LinkViewModel
import org.openapitools.client.models.CreateCardEntity
import org.openapitools.client.models.CreateCategoryEntity
import org.openapitools.client.models.CreateLinkEntity
import java.util.*

class CreateLinkViewModel(
    val card: UUID,
    private val linkRepository: LinkRepository = LinkRepository(),
    private val cardRepository: CardRepository = CardRepository()
) : UpdateAndCreateLinkViewModel(
    cardId = card,
) {

    val link = mutableStateOf(listOf<LinkSelectItem>())

    init {
        cardRepository.getPage(null,null,null,false,
            onSuccess = {
                errors.value = emptyList()
                    allCards.value = it
                        .filter { card ->
                            card.id != cardId
                        }
                        .map { card ->
                            CardSelectItem(
                                card = card,
                                isChecked = false
                            )
                        }
                },

            onFailure = {
                error.value = "Check network connection"
            })
    }

    override fun save() {
        error.value = null
        errors.value = emptyList()

        val target = mutableStateOf<UUID?>(null)

        allCards.value
            .filter { it.isChecked }
            .let {
                target.value = it[0].card.id

                CreateLinkEntity(
                    name = linkName.value,
                    source = linkSource.value!!,
                    target = target.value!!
                )
            }
            .let {
                linkRepository.createLink(
                    link = it,
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
    }



    /* ONLY THIS
    override fun save() {
        error.value = null
        errors.value = emptyList()

        linkRepository.createLink(
                    link = CreateLinkEntity(
                        name = linkName.value,
                        source = linkSource!!,
                        target = linkTarget.value!!),
                    onSuccess = {
                        isFinished.value = true
                    },
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
     */
}
