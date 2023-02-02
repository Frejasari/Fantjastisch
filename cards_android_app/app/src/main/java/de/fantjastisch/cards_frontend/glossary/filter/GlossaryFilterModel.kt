package de.fantjastisch.cards_frontend.glossary.filter

import de.fantjastisch.cards_frontend.category.CategoryRepository
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
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
    suspend fun getCategories(): RepoResult<List<CategoryEntity>> {
        return categoryRepository.getPage()
    }
}