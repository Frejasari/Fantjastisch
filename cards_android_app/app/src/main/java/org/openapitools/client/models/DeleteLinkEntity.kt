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
 * @param id The UUID of the link that is to be deleted.
 */


data class DeleteLinkEntity (

    /* The UUID of the link that is to be deleted. */
    @Json(name = "id")
    val id: java.util.UUID

)

