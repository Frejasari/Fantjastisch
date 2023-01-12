package de.fantjastisch.cards_backend.category.aggregate;

import de.fantjastisch.cards_backend.util.validation.Commandable;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

/**
 * Diese Klasse stellt ein CRUD-Kommando zum Aktualisieren einer Kategorie-Entität dar.
 * Eine Instanz dieser Klasse wird als Parameter vom entsprechenden API-Endpunkt entgegengenommen.
 * @Author Semjon Nirmann
 */
@Data
@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCategory implements Commandable {
    @NotNull
    @ApiModelProperty(
            value = "The UUID of the category that is to be updated.",
            required = true,
            example = "dce61f5d-93f8-421d-9552-5567d707b650"
    )
    private UUID id;

    @NotBlank
    @NotNull
    @ApiModelProperty(
            value = "The category label",
            required = true,
            example = "Category #1")
    private String label;

    @NotNull
    @ApiModelProperty(
            value = "An array of child-category UUIDs.",
            required = true,
            example = "[fa9d9f26-8e58-4653-809c-c3b5d8e7d97f]")
    private List<UUID> subCategories;
}
