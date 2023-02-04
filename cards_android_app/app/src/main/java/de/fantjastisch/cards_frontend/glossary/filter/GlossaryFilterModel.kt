package de.fantjastisch.cards_frontend.glossary.filter

import de.fantjastisch.cards_frontend.category.CategoryRepository
import de.fantjastisch.cards_frontend.util.RepoResult
import org.openapitools.client.models.CategoryEntity

/**
 * Kapselt die Logik für das [GlossaryFilterViewModel].
 * Fungiert als Vermittler zwischen Repository und ViewModel.
 *
 * @property categoryRepository Repository Kategorien
 *
 * @author Freja Sender, Tamari Bayer
 */
class GlossaryFilterModel(
    private val categoryRepository: CategoryRepository = CategoryRepository()
) {
    /**
     * Sendet eine Anfrage an das Backend-Repository für Kategorien.
     * Im Erfolgsfall werden alle Kategorien geholt.
     *
     * @return RepoResult<Unit> (OnSuccess, OnUnexpectedError, ...)
     */
    suspend fun getCategories(): RepoResult<List<CategoryEntity>> {
        return categoryRepository.getPage()
    }
}