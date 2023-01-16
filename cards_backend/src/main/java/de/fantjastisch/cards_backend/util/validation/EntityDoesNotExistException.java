package de.fantjastisch.cards_backend.util.validation;

import lombok.Getter;

import java.util.UUID;

/**
 * Wird geworfen, wenn eine angefragte Entität nicht gefunden werden kann.
 *
 * @author Freja Sender
 */
@Getter
public class EntityDoesNotExistException extends RuntimeException {
    private UUID id;
    private String field;

    public EntityDoesNotExistException(UUID id, String field) {
        this.id = id;
        this.field = field;
    }
}
