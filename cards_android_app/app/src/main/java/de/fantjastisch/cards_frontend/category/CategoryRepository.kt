package de.fantjastisch.cards_frontend.category

import de.fantjastisch.cards_frontend.config.client
import de.fantjastisch.cards_frontend.util.RepoResult
import de.fantjastisch.cards_frontend.util.toRepoResult
import org.openapitools.client.apis.CategoryApi
import org.openapitools.client.models.CategoryEntity
import org.openapitools.client.models.CreateCategoryEntity
import org.openapitools.client.models.UpdateCategoryEntity
import retrofit2.awaitResponse
import java.util.*

/**
 * Repository kommuniziert mit dem CategoryBackend.
 *
 * @author Semjon Nirmann, Freja Sender
 */
class CategoryRepository {

    private val service = client.createService(CategoryApi::class.java)

    /**
     * Sendet eine Datenbankanfrage an das Backend und kriegt im Erfolgsfall für die
     * übergebene Id, die passende Kategorie.
     *
     * @param id Id, der gesuchten Kategorie.
     * @return RepoResult<CategoryEntity> OnSuccess: Kategorie als [CategoryEntity]-Entität.
     */
    suspend fun getCategory(
        id: UUID
    ): RepoResult<CategoryEntity> = service.getCategory(id).awaitResponse().toRepoResult()

    /**
     * Sendet eine Anfrage an das Backend und kriegt im Erfolgsfall alle
     * vorhandenen Kategorien zurück.
     *
     * @return RepoResult<List<CategoryEntity>> OnSuccess: Liste an Kategorien.
     */
    suspend fun getPage(
    ): RepoResult<List<CategoryEntity>> = service.getCategoryPage().awaitResponse().toRepoResult()

    /**
     * Sendet eine Anfrage an das Backend, um eine Kategorie in die Datenbank zu speichern.
     *
     * @param category Kategorie, welche erzeugt werden soll.
     * @return RepoResponse<Unit> (OnSuccess, OnUnexpectedError, ...)
     */
    suspend fun createCategory(
        category: CreateCategoryEntity
    ): RepoResult<String> = service.createCategory(category).awaitResponse().toRepoResult()

    /**
     * Sendet eine Anfrage an das Backend, um eine bestehende Kategorie in der Datenbank zu überschreiben.
     *
     * @param category Kategorie, welche überschrieben werden soll.
     * @return RepoResponse<Unit> (OnSuccess, OnUnexpectedError, ...)
     */
    suspend fun updateCategory(
        category: UpdateCategoryEntity
    ): RepoResult<Unit> = service.updateCategory(category).awaitResponse().toRepoResult()

    /**
     * Sendet eine Anfrage an das Backend, um eine bestehende Kategorie aus der Datenbank zu löschen.
     *
     * @param categoryId Id der Kategorie, welche gelöscht werden soll.
     * @return RepoResponse<Unit> (OnSuccess, OnUnexpectedError, ...)
     */
    suspend fun deleteCategory(
        categoryId: UUID
    ): RepoResult<Unit> = service.deleteCategory(categoryId).awaitResponse().toRepoResult()
}
