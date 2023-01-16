package de.fantjastisch.cards_backend.util.validation.errors;

import lombok.Getter;

/**
 * Eine Aufzählung konstanter Fehler-Codes, welche von einem {@link ErrorEntry}-Objekt gesammelt werden und der
 * Aufrufer*in eines CRUD API-Endpunktes mitgeteilt werden sollen, sofern eine Rahmenbedingung der Validierung verletzt wurde.
 *
 * @Author Semjon Nirmann
 */
@Getter
public enum ErrorCode {
    ENTITY_DOES_NOT_EXIST("Entity does not exist"),
    CONSTRAINT_VIOLATION("Unknown error - a constraint has been broken"),
    NO_CATEGORIES_VIOLATION("No categories found"),
    NOT_NULL_VIOLATION("Field must not be null"),
    NOT_BLANK_VIOLATION("Field must not be blank"),
    LABEL_TAKEN_VIOLATION("Label is already taken - no duplicates allowed"),
    CATEGORY_DOESNT_EXIST_VIOLATION("Requested category does not exist"),
    SUBCATEGORY_DOESNT_EXIST_VIOLATION("Subcategory does not exist"),
    CATEGORY_NOT_EMPTY_VIOLATION("Cannot delete non-empty category"),
    CYCLIC_SUBCATEGORY_RELATION_VIOLATION("Cycle detected in subcategory hierarchy - cannot add parent as child"),
    SUBCATEGORY_IS_NULL_VIOLATION("Cannot pass empty or null-valued subcategories"),
    CARD_DUPLICATE_VIOLATION("Another card with equivalent attributes already exists");

    public final String msg;

    private ErrorCode(final String msg) {
        this.msg = msg;
    }
}