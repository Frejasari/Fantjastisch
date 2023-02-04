package de.fantjastisch.cards_frontend.category.overview

import de.fantjastisch.cards_frontend.category.CategoryRepository
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import java.util.*

/**
 * Kapselt die Logik für das [CategoryOverviewViewModel].
 * Fungiert als Vermittler zwischen Repository und ViewModel.
 *
 * @property categoryRepository Kategorie Repository.
 *
 * @author Tamari Bayer
 */
class CategoryOverviewModel(
    private val categoryRepository: CategoryRepository = CategoryRepository(),
) {

    /**
     * Sendet eine Anfrage an das [CategoryRepository] und kriegt alle Kategorien zurück.
     *
     * @return RepoResult<List<CategoryEntity>> OnSuccess: Eine Liste aller Kategorien.
     */
    suspend fun getCategories() = categoryRepository.getPage()

    /**
     * Sendet eine Anfrage an das [CategoryRepository] um eine Kategorie zu löschen.
     *
     * @param id Id der Kategorie, welche gelöscht werden soll.
     * @return RepoResult<Unit>
     */
    suspend fun deleteCategory(id: UUID): RepoResult<Unit> =
        categoryRepository.deleteCategory(categoryId = id)
}