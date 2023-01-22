package de.fantjastisch.cards_frontend.card.create

import androidx.lifecycle.ViewModel
import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.category.CategoryRepository
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import org.openapitools.client.models.CreateCardEntity

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

    suspend fun createCard(
        answer: String,
        question: String,
        tag: String,
        categories: List<CategorySelectItem>
    ) = cardRepository.createCard(
        card = CreateCardEntity(
            question = question,
            answer = answer,
            tag = tag,
            categories = categories
                .filter { it.isChecked }
                .map { it.id }
        )
    )
}
