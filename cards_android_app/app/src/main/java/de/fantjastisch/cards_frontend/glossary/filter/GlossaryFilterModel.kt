package de.fantjastisch.cards_frontend.glossary.filter

import de.fantjastisch.cards_frontend.category.CategoryRepository
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import org.openapitools.client.models.CategoryEntity

class GlossaryFilterModel(
    private val categoryRepository: CategoryRepository = CategoryRepository()
) {
    suspend fun getCategories(): RepoResult<List<CategoryEntity>> {
        return categoryRepository.getPage()
    }
}