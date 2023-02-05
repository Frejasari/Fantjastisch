package de.fantjastisch.cards_backend.category.repository;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

/**
 * Diese Klasse stellt das Modell einer Kategorie dar.
 *
 * @author Semjon Nirmann, Alexander Kück
 */
@Builder
@Data // getter, setter, toString
public class Category {
    UUID id;
    String label;
    List<UUID> subCategories;
}



