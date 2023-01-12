package de.fantjastisch.cards_backend.card.aggregate;

import de.fantjastisch.cards_backend.util.validation.Commandable;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

/**
 * Diese Klasse stellt ein CRUD-Kommando zum Erstellen einer Karteikarte-Entität dar.
 * Eine Instanz dieser Klasse wird als Parameter vom entsprechenden API-Endpunkt entgegengenommen.
 *
 * @Author Tamari Bayer, Jessica Repty, Freja Sender
 */

@Data
@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class CreateCard implements Commandable {

    @NotBlank
    @ApiModelProperty(
            value = "The question to be asked",
            required = true,
            example = "Who am I?")
    private String question;

    @NotBlank
    @ApiModelProperty(
            value = "The answer to the question",
            required = true,
            example = "I am me")
    private String answer;

    @NotBlank
    @ApiModelProperty(
            value = "Tag",
            example = "I am a tag")
    private String tag;

    @NotEmpty
    @ApiModelProperty(
            value = "Zugehörige Kategorien",
            required = true,
            example = "[3b1824120d6d4857843aedfc1973d323, 40ac4fcc97024a87b0bdbffe1f7f49f8]")
    private List<UUID> categories;
}
