package de.fantjastisch.cards_frontend.category.update

import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.card.update.UpdateCardViewModel
import de.fantjastisch.cards_frontend.category.CategoryRepository
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import kotlinx.coroutines.*
import org.openapitools.client.models.*
import java.util.*

/**
 * Kapselt die Logik für das [UpdateCardViewModel].
 * Fungiert als Vermittler zwischen Repository und ViewModel.
 *
 * @property id Id, der Kategorie, welche bearbeitet wird.
 * @property categoryRepository Kategorie Repository
 *
 * @author Tamari Bayer, Freja Sender
 */
class UpdateCategoryModel(
    private val id: UUID,
    private val categoryRepository: CategoryRepository = CategoryRepository()
) {

    suspend fun update(
        label: String,
        subCategories: List<CategorySelectItem>
    ): RepoResult<Unit> = categoryRepository.updateCategory(
        UpdateCategoryEntity(
            id = id,
            label = label,
            subCategories = subCategories.filter { cat -> cat.isChecked }.map { cat -> cat.id }
        )
    )

    data class UpdateCategory(
        val id: UUID,
        val label: String,
        val allCategories: List<CategoryEntity>,
        val subCategories: List<UUID>,
    )

    @Suppress("UNCHECKED_CAST")
    suspend fun initializePage(): RepoResult<UpdateCategory> = coroutineScope {
        // Runs coroutines in parallel and waits until all of them are done
        val (categoryResult, allCategoriesResult) = awaitAll(
            async { categoryRepository.getCategory(id = id) },
            async { categoryRepository.getPage() },
        )

        when {
            categoryResult is RepoResult.Success
                    && allCategoriesResult is RepoResult.Success -> {
                val category = categoryResult.result as CategoryEntity
                val categories = allCategoriesResult.result as List<CategoryEntity>
                RepoResult.Success(
                    UpdateCategory(
                        id = category.id,
                        label = category.label,
                        allCategories = categories,
                        subCategories = category.subCategories,
                    )
                )
            }
            else -> RepoResult.Error(emptyList())
        }
    }

    suspend fun getCategories(): List<CategorySelectItem>? {
        return when (val result = categoryRepository.getPage()) {
            is RepoResult.Success -> result.result
                .filter { cat -> cat.id != id }
                .map { cat ->
                    CategorySelectItem(
                        label = cat.label,
                        id = cat.id,
                        isChecked = false
                    )
                }
            is RepoResult.Error,
            is RepoResult.ServerError -> null // TODO
        }
    }
}
