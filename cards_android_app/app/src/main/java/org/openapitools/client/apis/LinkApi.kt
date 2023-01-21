package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Call
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.CreateLinkEntity
import org.openapitools.client.models.DeleteLinkEntity
import org.openapitools.client.models.ErrorResponseEntity
import org.openapitools.client.models.LinkEntity
import org.openapitools.client.models.UpdateLinkEntity

interface LinkApi {
    /**
     * Create a new Link
     * 
     * Responses:
     *  - 404: Not Found
     *  - 201: Created
     *  - 422: Unprocessable Entity
     *
     * @param createLinkEntity 
     * @return [Call]<[kotlin.String]>
     */
    @POST("link/create")
    fun createLink(@Body createLinkEntity: CreateLinkEntity): Call<kotlin.String>

    /**
     * Delete a link
     * 
     * Responses:
     *  - 404: Not Found
     *  - 200: OK
     *  - 422: Unprocessable Entity
     *  - 200: OK
     *
     * @param deleteLinkEntity 
     * @return [Call]<[Unit]>
     */
    @DELETE("link/delete")
    fun deleteLink(@Body deleteLinkEntity: DeleteLinkEntity): Call<Unit>

    /**
     * Get the Link from the given name and source
     * 
     * Responses:
     *  - 404: Not Found
     *  - 200: OK
     *  - 422: Unprocessable Entity
     *
     * @param id 
     * @return [Call]<[LinkEntity]>
     */
    @GET("link/get")
    fun getLink(@Query("id") id: java.util.UUID): Call<LinkEntity>

    /**
     * Get all links
     * 
     * Responses:
     *  - 404: Not Found
     *  - 200: OK
     *  - 422: Unprocessable Entity
     *
     * @param id 
     * @return [Call]<[kotlin.collections.List<LinkEntity>]>
     */
    @GET("link/getPage")
    fun getPage(@Query("id") id: java.util.UUID): Call<kotlin.collections.List<LinkEntity>>

    /**
     * Update a link
     * 
     * Responses:
     *  - 404: Not Found
     *  - 200: OK
     *  - 422: Unprocessable Entity
     *  - 200: OK
     *
     * @param updateLinkEntity 
     * @return [Call]<[Unit]>
     */
    @PUT("link/update")
    fun updateLink(@Body updateLinkEntity: UpdateLinkEntity): Call<Unit>

}
