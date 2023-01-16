package de.fantjastisch.cards_backend.util;

import de.fantjastisch.cards_backend.util.validation.errors.ErrorEntry;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;


/**
 * Standard-Rückgabeklasse für fehlerhafte Eingaben.
 *
 * @author Freja Sender
 */
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ErrorResponse {

    @Schema(required = true)
    private List<ErrorEntry> errors;

    public ErrorResponse(List<ErrorEntry> errors) {
        this.errors = errors;
    }
}