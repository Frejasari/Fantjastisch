package de.fantjastisch.cards_backend.card.aggregate;

import de.fantjastisch.cards_backend.card.Link;
import de.fantjastisch.cards_backend.util.validation.Commandable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;
import java.util.UUID;

/**
 * Diese Klasse stellt ein CRUD-Kommando zum Aktualisieren einer Karteikarte-Entität dar.
 * Eine Instanz dieser Klasse wird als Parameter vom entsprechenden API-Endpunkt entgegengenommen.
 *
 * @author Tamari Bayer, Jessica Repty, Freja Sender
 */

@SuppressWarnings("deprecation")
@Getter
@Builder
@ToString
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCard implements Commandable {

    @NotNull
    @Schema(
            description = "The UUID of the card that is to be updated.",
            required = true,
            example = "dce61f5d-93f8-421d-9552-5567d707b650")
    UUID id;


    @NotBlank
    @Schema(
            description = "The question to be asked",
            required = true,
            example = "Who am I?")
    String question;


    @NotBlank
    @Schema(
            description = "The answer to the question",
            required = true,
            example = "I am me")
    String answer;

    @NotBlank
    @Schema(
            description = "Tag",
            example = "I am a tag")
    String tag;

    @NotEmpty
    @Schema(
            description = "Zugehörige Kategorien",
            required = true,
            example = "[3b1824120d6d4857843aedfc1973d323, 40ac4fcc97024a87b0bdbffe1f7f49f8]")
    Set<UUID> categories;


    @NotNull
    @Schema(
            required = true,
            example = "[3b1824120d6d4857843aedfc1973d323, 40ac4fcc97024a87b0bdbffe1f7f49f8]")
    Set<Link> links;

}
