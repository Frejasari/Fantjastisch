/**
 *
 * Please note:
 * This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * Do not edit this file manually.
 *
 */

@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package org.openapitools.client.models


import com.squareup.moshi.Json

/**
 * 
 *
 * @param id 
 * @param question 
 * @param answer 
 * @param tag 
 * @param categories 
 */


data class CardEntity (

    @Json(name = "id")
    val id: java.util.UUID,

    @Json(name = "question")
    val question: kotlin.String,

    @Json(name = "answer")
    val answer: kotlin.String,

    @Json(name = "tag")
    val tag: kotlin.String,

    @Json(name = "categories")
    val categories: kotlin.collections.List<java.util.UUID>

)

