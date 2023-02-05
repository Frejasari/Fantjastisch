package de.fantjastisch.cards_backend.util.validation.errors;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Diese Klasse dient dem Erzeugen detaillierter Fehlermeldungen, welche von einer
 * {@link de.fantjastisch.cards_backend.util.validation.CommandValidationException}-Instanz
 * übermittelt werden können und beim Validieren von CRUD-Kommando-Objekten entstehen können.
 *
 * @author Semjon Nirmann, Freja Sender
 */
@SuppressWarnings("deprecation")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class ErrorEntry {

    @Schema(example = "NOT_NULL_VIOLATION", required = true)
    private ErrorCode code;

    @Schema(example = "Entity does not exist", required = true)
    private String message;

    @Schema(example = "name", required = true)
    private String field;

}

