package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import org.openapitools.client.models.CategoryEntity
import org.openapitools.client.models.CreateCategoryEntity
import org.openapitools.client.models.UpdateCategoryEntity
import retrofit2.Call
import retrofit2.http.*

interface CategoryApi {
    /**
     * Create a new category
     *
     * Responses:
     *  - 201: Created
     *  - 404: Not Found
     *  - 422: Unprocessable Entity
     *
     * @param createCategoryEntity
     * @return [Call]<[kotlin.String]>
     */
    @POST("category/create")
    fun createCategory(@Body createCategoryEntity: CreateCategoryEntity): Call<kotlin.String>

    /**
     * Delete a category
     *
     * Responses:
     *  - 200: OK
     *  - 404: Not Found
     *  - 422: Unprocessable Entity
     *
     * @param id
     * @return [Call]<[Unit]>
     */
    @DELETE("category/delete")
    fun deleteCategory(@Query("id") id: java.util.UUID): Call<Unit>

    /**
     * Get specific category
     *
     * Responses:
     *  - 404: Not Found
     *  - 422: Unprocessable Entity
     *  - 200: OK
     *
     * @param id
     * @return [Call]<[CategoryEntity]>
     */
    @GET("category/get")
    fun getCategory(@Query("id") id: java.util.UUID): Call<CategoryEntity>

    /**
     * Get all categories
     *
     * Responses:
     *  - 200: OK
     *  - 404: Not Found
     *  - 422: Unprocessable Entity
     *
     * @return [Call]<[kotlin.collections.List<CategoryEntity>]>
     */
    @GET("category/getPage")
    fun getCategoryPage(): Call<kotlin.collections.List<CategoryEntity>>

    /**
     * Update a category
     *
     * Responses:
     *  - 200: OK
     *  - 404: Not Found
     *  - 422: Unprocessable Entity
     *
     * @param updateCategoryEntity
     * @return [Call]<[Unit]>
     */
    @PUT("category/update")
    fun updateCategory(@Body updateCategoryEntity: UpdateCategoryEntity): Call<Unit>

}
