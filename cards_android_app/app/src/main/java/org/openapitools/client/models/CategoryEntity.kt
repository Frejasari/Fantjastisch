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
 * @param subCategories 
 */

data class CategoryEntity (

    @Json(name = "id")
    val id: java.util.UUID? = null,

    @Json(name = "label")
    val label: kotlin.String? = null,

    @Json(name = "subCategories")
    val subCategories: kotlin.collections.List<java.util.UUID>? = null

)

