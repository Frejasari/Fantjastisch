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

import org.openapitools.client.models.ErrorEntryEntity

import com.squareup.moshi.Json

/**
 * 
 *
 * @param errors 
 */


data class ErrorResponseEntity (

    @Json(name = "errors")
    val errors: kotlin.collections.List<ErrorEntryEntity>

)

