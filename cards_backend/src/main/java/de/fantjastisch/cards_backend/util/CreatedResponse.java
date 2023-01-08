package de.fantjastisch.cards_backend.util;

import de.fantjastisch.cards_backend.util.CreatedResponse.CreatedResponseData;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

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
