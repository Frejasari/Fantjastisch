package de.fantjastisch.categorys_frontend.category

import org.openapitools.client.apis.CategoryApi
import org.openapitools.client.infrastructure.ApiClient
import org.openapitools.client.models.CategoryEntity
import org.openapitools.client.models.CreateCategoryEntity
import org.openapitools.client.models.UpdateCategoryEntity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


val client: ApiClient by lazy { ApiClient() }

fun <T> Call<T>.enqueue(onSuccess: (T) -> Unit, onFailure: () -> Unit) {
    enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            response.body()?.let(onSuccess) ?: onFailure()
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            t.printStackTrace()
            onFailure()
        }
    })
}


class CategoryRepository {

    val service = client.createService(CategoryApi::class.java)

    fun getCategory(id: UUID, onSuccess: (CategoryEntity) -> Unit, onFailure: () -> Unit) =
        service.getCategory(id).enqueue(onSuccess, onFailure)

    fun getPage(
        onSuccess: (List<CategoryEntity>) -> Unit,
        onFailure: () -> Unit
    ) = service.getCategoryPage().enqueue(onSuccess, onFailure)

    fun createCategory(
        category: CreateCategoryEntity,
        onSuccess: (String) -> Unit,
        onFailure: () -> Unit
    ) =
        service.createCategory(category).enqueue(onSuccess, onFailure)

    fun updateCategory(
        category: UpdateCategoryEntity,
        onSuccess: (Unit) -> Unit,
        onFailure: () -> Unit
    ) =
        service.updateCategory(category).enqueue(onSuccess, onFailure)
}