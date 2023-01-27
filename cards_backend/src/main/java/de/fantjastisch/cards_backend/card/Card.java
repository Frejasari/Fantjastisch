package de.fantjastisch.cards_backend.card;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

/**
 * Diese Klasse stellt das Modell einer Karteikarte dar, externe Kommunikation über die API.
 *
 * @author Tamari Bayer, Jessica Repty, Freja Sender
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
    Set<Category> categories;

    @Schema(required = true)
    Set<Link> links;


    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({"id", "label"})
    @Schema(name = "CategoryOfCard") // rename for client generation to avoid name clash
    public static class Category {

        @JsonProperty("id")
        @Schema(required = true)
        UUID id;

        @JsonProperty("label")
        @Schema(required = true)
        String label;
    }
}
