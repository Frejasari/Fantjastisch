package de.fantjastisch.cards_backend.util.validation.errors;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.ConstraintViolation;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Diese Klasse dient dem Erzeugen detaillierter Fehlermeldungen, welche von einer
 * {@link de.fantjastisch.cards_backend.util.validation.CommandValidationException}-Instanz
 * übermittelt werden können und beim Validieren von CRUD-Kommando-Objekten entstehen können.
 *
 * @author Semjon Nirmann
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@SuperBuilder
public class ErrorEntry {

    @Schema(example = "1002", required = true)
    private ErrorCode code;

    @Schema(example = "name", required = true)
    private String field;

    @JsonIgnore
    @Schema(hidden = true)
    private transient ConstraintViolation violation;

    /**
     * Erzeugt eine leicht-verständliche String-Repräsentation aus einer Liste von {@ErrorEntry}-Objekten.
     *
     * @param errors Die Liste von {@ErrorEntry}-Objekten.
     * @return Der String, welcher kurz zusammenfasst, welche Attribute fehlerhaft übermittelt wurden und welche
     * Rahmenbedingungen dabei nicht eingehalten wurden.
     */
    public static String mapErrorsToString(List<ErrorEntry> errors) {
        List<String> toStrings = errors.stream().map(x -> String.format("%s @ field: %s", x.getCode().getMsg(), x.getField())).collect(Collectors.toList());
        return String.join(" | ", toStrings);
    }
}

