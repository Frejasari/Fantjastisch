package de.fantjastisch.cards_frontend.util

import de.fantjastisch.cards.R
import org.openapitools.client.models.ErrorEntryEntity

/**
 * Mappt die Fehlermeldungen aus dem Backend
 *
 * @param code die Fehlermeldung aus dem Backend
 *
 * @return Die ID der Fehlermeldung
 */
fun mapError(code: ErrorEntryEntity.Code): Int {
    return when (code) {
        ErrorEntryEntity.Code.cONSTRAINTVIOLATION -> R.string.error_not_empty
        ErrorEntryEntity.Code.nOCATEGORIESVIOLATION -> R.string.error_not_empty
        ErrorEntryEntity.Code.nOTNULLVIOLATION -> R.string.error_not_null
        ErrorEntryEntity.Code.nOTBLANKVIOLATION -> R.string.error_not_empty
        ErrorEntryEntity.Code.lABELTAKENVIOLATION -> R.string.error_label_taken
        ErrorEntryEntity.Code.cATEGORYDOESNTEXISTVIOLATION,
        ErrorEntryEntity.Code.sUBCATEGORYDOESNTEXISTVIOLATION -> R.string.error_category_does_not_exist
        ErrorEntryEntity.Code.cATEGORYNOTEMPTYVIOLATION -> R.string.error_no_category
        ErrorEntryEntity.Code.cYCLICSUBCATEGORYRELATIONVIOLATION -> R.string.error_no_cycles
        ErrorEntryEntity.Code.eNTITYDOESNOTEXIST -> R.string.error_entity_does_not_exist
        ErrorEntryEntity.Code.cARDDUPLICATEVIOLATION -> R.string.error_card_already_exists
        ErrorEntryEntity.Code.bOXLABELSISNULLVIOLATION -> R.string.error_box_labels_null
    }

}

/**
 * Stellt vordefinierte Konstanten für die Fehlermeldungen
 *
 * @property text Die ID der Fehlermeldung
 */
enum class ErrorsEnum(val text: Int) {
    NO_ERROR(0),
    LINK_ERROR(R.string.error_link_no_card),
    CATEGORY_NOT_EMPTY_ERROR(R.string.categories_not_empty_error),
    CARD_MUST_HAVE_CATEGORY_ERROR(R.string.categories_error),
    NETWORK(R.string.error_network),
    UNEXPECTED(R.string.error_unexpected),
    CHECK_INPUT(R.string.error_check_input),
    CYCLIC_CATEGORIE(R.string.error_no_cycles),

}