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
 * @param label The category label
 * @param subCategories An array of child-category UUIDs.
 */


data class CreateCategoryEntity (

    /* The category label */
    @Json(name = "label")
    val label: kotlin.String,

    /* An array of child-category UUIDs. */
    @Json(name = "subCategories")
    val subCategories: kotlin.collections.List<java.util.UUID>

)

