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

    fun getCategory(
        id: UUID,
        onSuccess: (CategoryEntity) -> Unit,
        onFailure: (errors: ErrorResponseEntity?) -> Unit
    ) =
        service.getCategory(id).enqueue(onSuccess, onFailure)

    suspend fun getPage(
    ) = service.getCategoryPage().awaitResponse().toRepoResponse()

    fun getPage(
        onSuccess: (List<CategoryEntity>) -> Unit,
        onFailure: (errors: ErrorResponseEntity?) -> Unit
    ) = service.getCategoryPage().enqueue(onSuccess, onFailure)

    fun createCategory(
        category: CreateCategoryEntity,
        onSuccess: (String) -> Unit,
        onFailure: (errors: ErrorResponseEntity?) -> Unit
    ) =
        service.createCategory(category).enqueue(onSuccess, onFailure)

    fun updateCategory(
        category: UpdateCategoryEntity,
        onSuccess: (Unit) -> Unit,
        onFailure: (errors: ErrorResponseEntity?) -> Unit
    ) =
        service.updateCategory(category).enqueue(onSuccess, onFailure)

    fun deleteCategory(
        categoryId: UUID,
        onSuccess: (Unit) -> Unit,
        onFailure: (errors: ErrorResponseEntity?) -> Unit
    ) =
        service.deleteCategory(categoryId).enqueue(onSuccess, onFailure)

}