package de.fantjastisch.cards_backend.category.aggregate;

import de.fantjastisch.cards_backend.util.validation.Commandable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;
import java.util.UUID;

/**
 * Diese Klasse stellt ein CRUD-Kommando zum Aktualisieren einer Kategorie-Entität dar.
 * Eine Instanz dieser Klasse wird als Parameter vom entsprechenden API-Endpunkt entgegengenommen.
 *
 * @author Semjon Nirmann
 */
@SuppressWarnings("deprecation")
@Getter
@Builder
@ToString
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCategory implements Commandable {
    @NotNull
    @Schema(
            description = "The UUID of the category that is to be updated.",
            required = true,
            example = "dce61f5d-93f8-421d-9552-5567d707b650"
    )
    private UUID id;

    @NotBlank
    @NotNull
    @Schema(
            description = "The category label",
            required = true,
            example = "Category #1")
    private String label;

    @NotNull
    @Schema(
            description = "An array of child-category UUIDs.",
            required = true,
            example = "[fa9d9f26-8e58-4653-809c-c3b5d8e7d97f]")
    private Set<UUID> subCategories;
}
