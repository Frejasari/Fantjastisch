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
 * @param label 
 * @param boxLabels 
 */


data class LearningSystemEntity (

    @Json(name = "id")
    val id: java.util.UUID,

    @Json(name = "label")
    val label: kotlin.String,

    @Json(name = "boxLabels")
    val boxLabels: kotlin.collections.List<kotlin.String>

)

