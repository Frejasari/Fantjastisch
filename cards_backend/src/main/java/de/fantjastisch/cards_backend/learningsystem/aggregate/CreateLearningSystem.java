package de.fantjastisch.cards_backend.learningsystem.aggregate;

import de.fantjastisch.cards_backend.util.validation.Commandable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * Diese Klasse stellt ein CRUD-Kommando zum Erstellen einer Lernsystem-Entität dar.
 * Eine Instanz dieser Klasse wird als Parameter vom entsprechenden API-Endpunkt entgegengenommen.
 *
 * @author Semjon Nirmann, Jessica Repty, Alex Kück
 */

@SuppressWarnings("deprecation")
@Getter
@Builder
@ToString
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateLearningSystem implements Commandable {

    @NotBlank
    @Schema(
            description = "The learning system's label",
            required = true,
            example = "4-Boxen-System")
    String label;

    @NotNull
    @Schema(
            description = "A List of Strings representing box labels.",
            required = true,
            example = "[schlecht, mittel, gut, fantjastisch]")
    List<String> boxLabels;
}
