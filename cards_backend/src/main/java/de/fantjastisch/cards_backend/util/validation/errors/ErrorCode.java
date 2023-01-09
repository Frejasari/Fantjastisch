package de.fantjastisch.cards_backend.util.validation.errors;

public enum ErrorCode {
    CONSTRAINT_VIOLATION,
    NOT_NULL_VIOLATION,
    NOT_BLANK_VIOLATION,
    LABEL_TAKEN,
    QUESTION_DUPLICATE,
    SUBCATEGORY_DOESNT_EXIST,
    CATEGORY_NOT_EMPTY,
    CYCLIC_SUBCATEGORY_RELATION
}