package de.fantjastisch.cards_frontend.util

import org.openapitools.client.models.ErrorEntryEntity

fun mapError(code: ErrorEntryEntity.Code): String {
    return when (code) {
        ErrorEntryEntity.Code.cONSTRAINTVIOLATION -> "Feld darf nicht leer sein."
        ErrorEntryEntity.Code.nOCATEGORIESVIOLATION -> "Feld darf nicht leer sein."
        ErrorEntryEntity.Code.nOTNULLVIOLATION -> "Feld darf nicht leer sein."
        ErrorEntryEntity.Code.nOTBLANKVIOLATION -> "Feld darf nicht leer sein."
        ErrorEntryEntity.Code.lABELTAKENVIOLATION -> "Label ist bereits vergeben."
        ErrorEntryEntity.Code.cATEGORYDOESNTEXISTVIOLATION -> "Kategorie existiert nicht."
        ErrorEntryEntity.Code.sUBCATEGORYDOESNTEXISTVIOLATION -> "Kategorie existiert nicht."
        ErrorEntryEntity.Code.cATEGORYNOTEMPTYVIOLATION -> "Es muss eine Kategorie ausgewaehlt werden."
        ErrorEntryEntity.Code.cYCLICSUBCATEGORYRELATIONVIOLATION -> "Zyklen sind nicht erlaubt."
        ErrorEntryEntity.Code.sUBCATEGORYISNULLVIOLATION -> "Subkategorien dürfen nicht null sein."
        ErrorEntryEntity.Code.eNTITYDOESNOTEXIST -> "Entität exisitert nicht."
        ErrorEntryEntity.Code.cARDDUPLICATEVIOLATION -> "Karte existiert bereits."
        ErrorEntryEntity.Code.bOXLABELSISNULLVIOLATION -> "Die Box-Labels dürfen nicht leer sein."
    }
}