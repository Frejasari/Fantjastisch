package de.fantjastisch.cards_backend.learningsystem.aggregate;

import de.fantjastisch.cards_backend.util.validation.Commandable;
import io.swagger.v3.oas.annotations.media.Schema;
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
 * Diese Klasse stellt ein CRUD-Kommando zum Aktualisieren einer Lernsystem-Entität dar.
 * Eine Instanz dieser Klasse wird als Parameter vom entsprechenden API-Endpunkt entgegengenommen.
 *
 * @author Semjon Nirmann, Jessica Repty, Alex Kück
 */
@Data
@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLearningSystem implements Commandable {

    @NotNull
    @Schema(
            description = "The UUID of the learning system that is to be updated.",
            required = true,
            example = "dce61f5d-93f8-421d-9552-5567d707b650"
    )
    private UUID id;

    @NotBlank
    @NotNull
    @Schema(
            description = "The learning system's label",
            required = true,
            example = "4-boxen-system")
    private String label;

    @NotNull
    @Schema(
            description = "An array of child-category UUIDs.",
            required = true,
            example = "[schlecht, mittel, gut, fantjastisch]")
    private List<String> boxLabels;
}
