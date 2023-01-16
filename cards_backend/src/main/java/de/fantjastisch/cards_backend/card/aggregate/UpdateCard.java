package de.fantjastisch.cards_backend.card.aggregate;

import de.fantjastisch.cards_backend.card.Card;
import de.fantjastisch.cards_backend.util.validation.Commandable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

/**
 * Diese Klasse stellt ein CRUD-Kommando zum Aktualisieren einer Karteikarte-Entität dar.
 * Eine Instanz dieser Klasse wird als Parameter vom entsprechenden API-Endpunkt entgegengenommen.
 *
 * @author Tamari Bayer, Jessica Repty, Freja Sender
 */

@Data
@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCard implements Commandable {

    @NotNull
    @Schema(
            description = "The UUID of the card that is to be updated.",
            required = true,
            example = "dce61f5d-93f8-421d-9552-5567d707b650")
    private UUID id;


    @NotBlank
    @Schema(
            description = "The question to be asked",
            required = true,
            example = "Who am I?")
    private String question;


    @NotBlank
    @Schema(
            description = "The answer to the question",
            required = true,
            example = "I am me")
    private String answer;

    @NotBlank
    @Schema(
            description = "Tag",
            example = "I am a tag")
    private String tag;

    @NotNull
    @NotEmpty
    @Schema(
            description = "Zugehörige Kategorien",
            required = true,
            example = "[3b1824120d6d4857843aedfc1973d323, 40ac4fcc97024a87b0bdbffe1f7f49f8]")
    private List<UUID> categories;
}
