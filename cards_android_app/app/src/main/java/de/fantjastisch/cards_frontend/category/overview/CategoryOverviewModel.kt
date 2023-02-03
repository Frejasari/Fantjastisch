package de.fantjastisch.cards_frontend.category.overview

import androidx.lifecycle.ViewModel
import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.card.update.UpdateCardViewModel
import de.fantjastisch.cards_frontend.category.CategoryRepository
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.openapitools.client.models.CardEntity
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
) : ViewModel(){

    suspend fun getCategories() = categoryRepository.getPage()

    suspend fun deleteCategory(id: UUID): RepoResult<Unit> = categoryRepository.deleteCategory(categoryId = id)
}