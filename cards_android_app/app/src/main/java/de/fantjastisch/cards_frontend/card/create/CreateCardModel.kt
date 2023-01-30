package de.fantjastisch.cards_frontend.card.create

import androidx.lifecycle.ViewModel
import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.category.CategoryRepository
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import org.openapitools.client.models.CreateCardEntity
import org.openapitools.client.models.LinkEntity

class CreateCardModel(
    private val cardRepository: CardRepository = CardRepository(),
    private val categoryRepository: CategoryRepository = CategoryRepository()
) : ViewModel() {

    suspend fun getCategories(): List<CategorySelectItem>? {
        val result = categoryRepository.getPage()
        return when (result) {
            is RepoResult.Success -> result.result.map { cat ->
                CategorySelectItem(
                    id = cat.id,
                    label = cat.label,
                    isChecked = false
                )
            }
            is RepoResult.Error,
            is RepoResult.ServerError -> null // TODO

        }
    }

    suspend fun getCards(): List<CardSelectItem>? {
        return when (val result = cardRepository.getPage(null,null,null,false)) {
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

    suspend fun createCard(
        answer: String,
        question: String,
        tag: String,
        categories: List<CategorySelectItem>,
        links: List<LinkEntity>
    ) = cardRepository.createCard(
        card = CreateCardEntity(
            question = question,
            answer = answer,
            tag = tag,
            links = links,
            categories = categories
                .filter { it.isChecked }
                .map { it.id }
        )
    )
}
