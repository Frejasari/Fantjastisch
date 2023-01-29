package de.fantjastisch.cards_backend.category.aggregate;

import de.fantjastisch.cards_backend.util.validation.Commandable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Set;
import java.util.UUID;

/**
 * Diese Klasse stellt ein CRUD-Kommando zum Erstellen einer Kategorie-Entität dar.
 * Eine Instanz dieser Klasse wird als Parameter vom entsprechenden API-Endpunkt entgegengenommen.
 *
 * @author Semjon Nirmann
 */
@Data
@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class CreateCategory implements Commandable {

    @NotBlank
    @NotNull
    @Schema(
            description = "The category label",
            required = true,
            example = "Category #1")
    String label;

    @NotNull
    @Schema(
            description = "An array of child-category UUIDs.",
            required = true,
            example = "[fa9d9f26-8e58-4653-809c-c3b5d8e7d97f]")
    Set<UUID> subCategories;
}
