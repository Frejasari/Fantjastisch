package de.fantjastisch.cards_frontend.link.create

import androidx.lifecycle.ViewModel
import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.category.CategoryRepository
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import de.fantjastisch.cards_frontend.link.LinkRepository
import org.openapitools.client.models.CreateCardEntity
import org.openapitools.client.models.CreateLinkEntity
import java.util.*

class CreateLinkModel(
    private val cardRepository: CardRepository = CardRepository(),
    private val linkRepository: LinkRepository = LinkRepository()
) : ViewModel() {

    suspend fun getCards(): List<CardSelectItem>? {
        val result = cardRepository.getPage(null,null,null,false)
        return when (result) {
            is RepoResult.Success -> result.result.map { card ->
                CardSelectItem(
                    card = card,
                    isChecked = false
                )
            }
            is RepoResult.Error,
            is RepoResult.ServerError -> null // TODO

        }
    }

    suspend fun createLink(
        name: String,
        sourceId: UUID,
        targetId: UUID,
    ) = linkRepository.createLink(
        link = CreateLinkEntity(
            name = name,
            source = sourceId,
            target = targetId
        )
    )
}
