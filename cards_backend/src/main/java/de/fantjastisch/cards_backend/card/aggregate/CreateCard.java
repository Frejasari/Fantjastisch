package de.fantjastisch.cards_backend.card.aggregate;

import de.fantjastisch.cards_backend.card.Link;
import de.fantjastisch.cards_backend.util.validation.Commandable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Set;
import java.util.UUID;

/**
 * Diese Klasse stellt ein CRUD-Kommando zum Erstellen einer Karteikarte-Entität dar.
 * Eine Instanz dieser Klasse wird als Parameter vom entsprechenden API-Endpunkt entgegengenommen.
 *
 * @author Tamari Bayer, Jessica Repty, Freja Sender
 */

@Data
@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateCard implements Commandable {

    @NotBlank
    @Schema(
            required = true,
            example = "Who am I?")
    private String question;

    @NotBlank
    @Schema(
            required = true,
            example = "I am me")
    private String answer;

    @NotBlank
    @Schema(
            required = true,
            example = "wichtig")
    private String tag;

    @NotEmpty
    @Schema(
            required = true,
            example = "[3b1824120d6d4857843aedfc1973d323, 40ac4fcc97024a87b0bdbffe1f7f49f8]")
    private Set<UUID> categories;

    @NotNull
    @Schema(
            required = true,
            example = "[3b1824120d6d4857843aedfc1973d323, 40ac4fcc97024a87b0bdbffe1f7f49f8]")
    // use Set to avoid duplicates
    private Set<Link> links;

}
