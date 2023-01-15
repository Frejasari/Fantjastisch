package de.fantjastisch.cards_backend.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * Diese Klasse stellt das Modell einer Kategorie dar.
 *
 * @Author Semjon Nirmann, Alexander Kück
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Schema(required = true)
    UUID id;

    @Schema(required = true)
    String label;

    @Schema(required = true)
    List<UUID> subCategories;
}
