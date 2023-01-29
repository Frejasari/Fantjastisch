package de.fantjastisch.cards_backend.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;
import java.util.UUID;

/**
 * Diese Klasse stellt das Modell einer Kategorie dar.
 *
 * @author Semjon Nirmann, Alexander Kück
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category {

    @Schema(required = true)
    UUID id;

    @Schema(required = true)
    String label;

    @Schema(required = true)
    Set<UUID> subCategories;
}
