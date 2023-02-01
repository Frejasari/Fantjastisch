package de.fantjastisch.cards_frontend.category

import de.fantjastisch.cards_frontend.infrastructure.client
import de.fantjastisch.cards_frontend.infrastructure.enqueue
import de.fantjastisch.cards_frontend.infrastructure.toRepoResponse
import org.openapitools.client.apis.CategoryApi
import org.openapitools.client.models.CategoryEntity
import org.openapitools.client.models.CreateCategoryEntity
import org.openapitools.client.models.ErrorResponseEntity
import org.openapitools.client.models.UpdateCategoryEntity
import retrofit2.awaitResponse
import java.util.*

class CategoryRepository {

    private val service = client.createService(CategoryApi::class.java)

    suspend fun getCategory(
        id: UUID
    ) = service.getCategory(id)
        .awaitResponse()
        .toRepoResponse()

    suspend fun getPage(
    ) = service.getCategoryPage().awaitResponse().toRepoResponse()


    suspend fun createCategory(
        category: CreateCategoryEntity
    ) = service.createCategory(category)
        .awaitResponse()
        .toRepoResponse()

    suspend fun updateCategory(
        category: UpdateCategoryEntity
    ) = service.updateCategory(category)
        .awaitResponse()
        .toRepoResponse()

    suspend fun deleteCategory(
        categoryId: UUID
    ) = service.deleteCategory(categoryId)
        .awaitResponse()
        .toRepoResponse()

}
