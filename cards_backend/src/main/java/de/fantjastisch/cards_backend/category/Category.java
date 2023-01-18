package de.fantjastisch.cards_backend.category;

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
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "label"})
@Schema(name = "Category") // rename for client generation to avoid name clash
public class Category {

    @JsonProperty("id")
    @Schema(required = true)
    UUID id;

    @JsonProperty("label")
    @Schema(required = true)
    String label;


    @Schema(required = true)
    List<Category> subCategories;
/*
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({"id", "label"})
    @Schema(name = "SubCategory") // rename for client generation to avoid name clash
    public static class Category {

        @JsonProperty("id")
        @Schema(required = true)
        UUID id;

        @JsonProperty("label")
        @Schema(required = true)
        String label;
    }*/
}
