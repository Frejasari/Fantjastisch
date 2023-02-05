package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Call
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.CreateLearningSystemEntity
import org.openapitools.client.models.ErrorResponseEntity
import org.openapitools.client.models.LearningSystemEntity
import org.openapitools.client.models.UpdateLearningSystemEntity

interface LearningSystemApi {
    /**
     * Create a new learning system
     * 
     * Responses:
     *  - 201: Created
     *  - 404: Not Found
     *  - 422: Unprocessable Entity
     *
     * @param createLearningSystemEntity 
     * @return [Call]<[kotlin.String]>
     */
    @POST("learningSystem/create")
    fun createLearningSystem(@Body createLearningSystemEntity: CreateLearningSystemEntity): Call<kotlin.String>

    /**
     * Delete a learning system
     * 
     * Responses:
     *  - 200: OK
     *  - 404: Not Found
     *  - 422: Unprocessable Entity
     *
     * @param id 
     * @return [Call]<[Unit]>
     */
    @DELETE("learningSystem/delete")
    fun deleteLearningSystem(@Query("id") id: java.util.UUID): Call<Unit>

    /**
     * Get specific learning system
     * 
     * Responses:
     *  - 404: Not Found
     *  - 422: Unprocessable Entity
     *  - 200: OK
     *
     * @param id 
     * @return [Call]<[LearningSystemEntity]>
     */
    @GET("learningSystem/get")
    fun getLearningSystem(@Query("id") id: java.util.UUID): Call<LearningSystemEntity>

    /**
     * Get all learning systems
     * 
     * Responses:
     *  - 200: OK
     *  - 404: Not Found
     *  - 422: Unprocessable Entity
     *
     * @return [Call]<[kotlin.collections.List<LearningSystemEntity>]>
     */
    @GET("learningSystem/getPage")
    fun getLearningSystemList(): Call<kotlin.collections.List<LearningSystemEntity>>

    /**
     * Update a learning system
     * 
     * Responses:
     *  - 200: OK
     *  - 404: Not Found
     *  - 422: Unprocessable Entity
     *
     * @param updateLearningSystemEntity 
     * @return [Call]<[Unit]>
     */
    @PUT("learningSystem/update")
    fun updateLearningSystem(@Body updateLearningSystemEntity: UpdateLearningSystemEntity): Call<Unit>

}
