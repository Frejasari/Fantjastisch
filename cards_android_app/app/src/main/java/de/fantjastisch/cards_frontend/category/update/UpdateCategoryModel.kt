package de.fantjastisch.cards_frontend.category.update

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

    /**
     * Sendet eine Anfrage an das [categoryRepository] für das Updaten einer Kategorie.
     *
     * @param label Neues Label der Kategorie.
     * @param subCategories Neue Unterkategorien der Kategorie.
     * @return RepoResult<Unit> (OnSuccess, OnUnexpectedError, ...)
     */
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

    /**
     * Hält die Informationen für eine [UpdateCategoryEntity].
     *
     * @property id Id der Kategorie.
     * @property label Label der Kategorie.
     * @property allCategories Alle Kategorien.
     * @property subCategories Unterkategorien der Kategorie.
     */
    data class UpdateCategory(
        val id: UUID,
        val label: String,
        val allCategories: List<CategoryEntity>,
        val subCategories: List<UUID>,
    )

    /**
     * Sendet eine Anfrage an das [categoryRepository] und kriegt im Erfolgsfall die zu bearbeitende
     * Kategorie, sowie alle Kategorien zurück.
     *
     * @return RepoResult<UpdateCategory> OnSuccess: Eine [UpdateCategoryEntity]-Entität.
     */
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

    /**
     * Sendet eine Anfrage an das [categoryRepository] und kriegt im Erfolgsfall alle Kategorien zurück.
     *
     * @return Eine Liste aller Kategorien, jeweils als [CategorySelectItem]-Entität.
     */
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
