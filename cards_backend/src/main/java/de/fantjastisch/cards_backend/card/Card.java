package de.fantjastisch.cards_backend.card;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

/**
 * Diese Klasse stellt das Modell einer Karteikarte dar.
 *
 * @Author Tamari Bayer, Jessica Repty, Freja Sender
 */
@Builder
@Data // getter, setter, toString
public class Card {
    @Schema(required = true)
    UUID id;
    @Schema(required = true)
    String question;
    @Schema(required = true)
    String answer;
    @Schema(required = true)
    String tag;
    @Schema(required = true)
    List<UUID> categories;
}
