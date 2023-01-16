package de.fantjastisch.cards_backend.util;

import de.fantjastisch.cards_backend.util.CreatedResponse.CreatedResponseData;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

/**
 * Diese Klasse wird von Controller-Objekten beim Erstellen einer Modell-Entität genutzt.
 * Sie gibt einen 201 HTTP-Status-Code zurück, wenn die Operation erfolgreich beendet wird und kommuniziert der
 * Aufrufer*in des API-Endpunktes die ID der eingefügten Entität.
 *
 * @author Freja Sender
 */
public class CreatedResponse extends ResponseEntity<CreatedResponseData> {

    public CreatedResponse(final UUID id) {
        super(new CreatedResponseData(id), HttpStatus.CREATED);
    }

    @Getter
    @Setter
    @EqualsAndHashCode
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreatedResponseData {

        UUID id;
    }
}
