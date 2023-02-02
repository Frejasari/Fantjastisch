package de.fantjastisch.cards_frontend.category.create

import androidx.lifecycle.ViewModel
import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.card.create.CreateCardViewModel
import de.fantjastisch.cards_frontend.category.CategoryRepository
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import org.openapitools.client.models.CategoryEntity
import org.openapitools.client.models.CreateCardEntity
import org.openapitools.client.models.CreateCategoryEntity
import org.openapitools.client.models.LinkEntity
import java.util.*

/**
 * Kapselt die Logik für das [CreateCategoryViewModel].
 * Fungiert als Vermittler zwischen Repository und ViewModel.
 *
 * @property categoryRepository Kategorie Repository
 *
 * @author Tamari Bayer, Freja Sender
 */
class CreateCategoryModel(
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

    suspend fun createCategory(
        label: String,
        subCategories: List<CategorySelectItem>
    ) = categoryRepository.createCategory(
        category = CreateCategoryEntity(
            label = label,
            subCategories = subCategories
                .filter { it.isChecked }
                .map { it.id }
        )
    )
}
