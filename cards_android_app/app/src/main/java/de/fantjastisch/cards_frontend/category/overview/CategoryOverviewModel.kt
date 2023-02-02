package de.fantjastisch.cards_frontend.category.overview

import de.fantjastisch.cards_frontend.category.CategoryRepository
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.util.*

class CategoryOverviewModel(
    private val categoryRepository: CategoryRepository = CategoryRepository(),
) {
    suspend fun getCategories() = categoryRepository.getPage()

    suspend fun deleteCategory(id: UUID): RepoResult<Unit> = coroutineScope {
        val (apiResult) = awaitAll(
            async { categoryRepository.deleteCategory(categoryId = id) },
        )
        when {
            apiResult is RepoResult.Success-> {
                RepoResult.Success(Unit)
            }
            else -> RepoResult.Error(emptyList())
        }
    }
}