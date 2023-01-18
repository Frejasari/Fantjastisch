package de.fantjastisch.cards_backend.category.repository;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
 * @author Semjon Nirmann, Alexander Kück
 */
@Builder
@Data // getter, setter, toString
public class Category {
    UUID id;
    String label;
    List<UUID> subCategories;
}



