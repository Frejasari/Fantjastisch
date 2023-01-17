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

/**
 * Diese Klasse stellt ein CRUD-Kommando zum Erstellen einer Lernsystem-Entität dar.
 * Eine Instanz dieser Klasse wird als Parameter vom entsprechenden API-Endpunkt entgegengenommen.
 *
 * @author Semjon Nirmann, Jessica Repty, Alex Kück
 */
@Data
@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class CreateLearningSystem implements Commandable {

    @NotBlank
    @NotNull
    @Schema(
            description = "The learning system's label",
            required = true,
            example = "4-Boxen-System")
    private String label;

    @NotNull
    @Schema(
            description = "A List of Strings representing box labels.",
            required = true,
            example = "[schlecht, mittel, gut, fantjastisch]")
    private List<String> boxLabels;
}
