package de.fantjastisch.cards_frontend.category.create

import de.fantjastisch.cards_frontend.category.CategoryRepository
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.util.RepoResult
import de.fantjastisch.cards_frontend.util.toUnselectedCategorySelectItems
import org.openapitools.client.models.CreateCategoryEntity

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
) {

    /**
     * Sendet eine Anfrage an das [categoryRepository] und kriegt im Erfolgsfall alle Kategorien zurück.
     *
     * @return RepoResult<List<CategoryEntity>> OnSuccess: Eine Liste aller Kategorien.
     */
    suspend fun getCategories(): RepoResult<List<CategorySelectItem>> {
        return when (val result = categoryRepository.getPage()) {
            is RepoResult.Success -> {
                val categorySelectItems = result.result.toUnselectedCategorySelectItems()
                return RepoResult.Success(categorySelectItems)
            }
            is RepoResult.Error -> RepoResult.Error(result.errors)
            is RepoResult.ServerError -> RepoResult.ServerError()
        }
    }

    /**
     * Sendet eine Anfrage an das [categoryRepository] für das Erstellen einer Karte.
     *
     * @param label Label der zu erstellenden Kategorie.
     * @param subCategories Zugehörige Unterkategorien der zu erstellenden Kategorie.
     *
     * @return RepoResult<String> (OnSuccess, OnUnexpectedError, ...)
     */
    suspend fun createCategory(
        label: String,
        subCategories: List<CategorySelectItem>
    ): RepoResult<String> = categoryRepository.createCategory(
        category = CreateCategoryEntity(
            label = label,
            subCategories = subCategories
                .filter { it.isChecked }
                .map { it.id }
        )
    )
}
