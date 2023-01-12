package de.fantjastisch.cards_backend.util.validation;

import de.fantjastisch.cards_backend.util.validation.errors.ErrorEntry;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Bündelt sämtliche Fehlermeldungen, die im Rahmen der Validierung von CRUD-Kommando-Objekten entstehen können.
 * Diese Exception wird in den Controller-Objekten eines Modells geworfen.
 * Hält eine Liste von {@link ErrorEntry} Objekten, welche Auskunft über das fehlerhafte Attribut sowie den konkreten
 * Fehler geben.
 *
 * @author Semjon Nirmann
 */
@Getter
public class CommandValidationException extends RuntimeException{

    private final transient List<ErrorEntry> errors;

    /**
     * Konstruktor für eine CommandValidationException.
     * Erhält einen oder mehrere {@link ErrorEntry}s, und fügt sie in das Attribut errors ein.
     * @param errors Eine Liste von Fehlermeldungen, welche zusammen weitergegeben werden sollen.
     */
    public CommandValidationException(final ErrorEntry... errors) {
        this.errors = Arrays.stream(errors).collect(Collectors.toList());
    }
}
