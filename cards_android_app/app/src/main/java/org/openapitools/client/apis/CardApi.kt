package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Call
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.CardEntity
import org.openapitools.client.models.CreateCardEntity
import org.openapitools.client.models.ErrorResponseEntity
import org.openapitools.client.models.UpdateCardEntity

interface CardApi {
    /**
     * 
     * Create a new Card
     * Responses:
     *  - 404: Not Found
     *  - 201: Created
     *  - 422: Unprocessable Entity
     *
     * @param createCardEntity 
     * @return [Call]<[kotlin.String]>
     */
    @POST("card/create")
    fun createCard(@Body createCardEntity: CreateCardEntity): Call<kotlin.String>

    /**
     * 
     * Delete a card
     * Responses:
     *  - 404: Not Found
     *  - 200: OK
     *  - 422: Unprocessable Entity
     *
     * @param id 
     * @return [Call]<[Unit]>
     */
    @DELETE("card/delete")
    fun deleteCard(@Query("id") id: java.util.UUID): Call<Unit>

    /**
     * 
     * Get the Card from the given Id
     * Responses:
     *  - 404: Not Found
     *  - 200: OK
     *  - 422: Unprocessable Entity
     *
     * @param id 
     * @return [Call]<[CardEntity]>
     */
    @GET("card/get")
    fun getCard(@Query("id") id: java.util.UUID): Call<CardEntity>

    /**
     * Get all cards
     * 
     * Responses:
     *  - 404: Not Found
     *  - 200: OK
     *  - 422: Unprocessable Entity
     *
     * @param categoryFilter  (optional)
     * @param search  (optional)
     * @param tag  (optional)
     * @param sort  (optional)
     * @return [Call]<[kotlin.collections.List<CardEntity>]>
     */
    @GET("card/getPage")
    fun getCardPage(@Query("categoryFilter") categoryFilter: kotlin.collections.List<java.util.UUID>? = null, @Query("search") search: kotlin.String? = null, @Query("tag") tag: kotlin.String? = null, @Query("sort") sort: kotlin.Boolean? = null): Call<kotlin.collections.List<CardEntity>>

    /**
     * 
     * Update a card
     * Responses:
     *  - 404: Not Found
     *  - 200: OK
     *  - 422: Unprocessable Entity
     *
     * @param updateCardEntity 
     * @return [Call]<[Unit]>
     */
    @PUT("card/update")
    fun updateCard(@Body updateCardEntity: UpdateCardEntity): Call<Unit>

}
