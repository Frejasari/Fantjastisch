package de.fantjastisch.cards_backend.util.validation.errors;

/**
 * Eine Aufzählung konstanter Fehler-Codes, welche von einem {@link ErrorEntry}-Objekt gesammelt werden und der
 * Aufrufer*in eines CRUD API-Endpunktes mitgeteilt werden sollen, sofern eine Rahmenbedingung der Validierung verletzt wurde.
 *
 * @Author Semjon Nirmann
 */
public enum ErrorCode {
    CONSTRAINT_VIOLATION,
    NO_CATEGORIES_VIOLATION,
    NOT_NULL_VIOLATION,
    NOT_BLANK_VIOLATION,
    LABEL_TAKEN_VIOLATION,
    CATEGORY_DOESNT_EXIST_VIOLATION,
    SUBCATEGORY_DOESNT_EXIST_VIOLATION,
    CATEGORY_NOT_EMPTY_VIOLATION,
    CYCLIC_SUBCATEGORY_RELATION_VIOLATION,
    SUBCATEGORY_IS_NULL_VIOLATION,
    CARD_DUPLICATE_VIOLATION
}