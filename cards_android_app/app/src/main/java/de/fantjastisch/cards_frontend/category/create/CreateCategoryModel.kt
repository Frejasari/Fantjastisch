package de.fantjastisch.cards_frontend.category.create

import androidx.lifecycle.ViewModel
import de.fantjastisch.cards_frontend.category.CategoryRepository
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
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
) : ViewModel() {

    /**
     * Sendet eine Anfrage an das [categoryRepository] und kriegt im Erfolgsfall alle Kategorien zurück.
     *
     * @return RepoResult<List<CategoryEntity>> OnSuccess: Eine Liste aller Kategorien.
     */
    suspend fun getCategories(): List<CategorySelectItem>? {
        return when (val result = categoryRepository.getPage()) {
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

    /**
     * Sendet eine Anfrage an das [categoryRepository] für das Erstellen einer Karte.
     *
     * @param label Label der zu erstellenden Kategorie.
     * @param subCategories Zugehörige Unterkategorien der zu erstellenden Kategorie.
     *
     * @return RepoResult<String> (OnSuccess, OnUnexpectedError, ...)
     * */
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
