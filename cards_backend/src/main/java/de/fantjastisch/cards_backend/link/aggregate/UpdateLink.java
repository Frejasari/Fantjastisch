package de.fantjastisch.cards_backend.link.aggregate;

import de.fantjastisch.cards_backend.util.validation.Commandable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/**
 * Diese Klasse stellt ein CRUD-Kommando zum Aktualisieren einer Link-Entität dar.
 * Eine Instanz dieser Klasse wird als Parameter vom entsprechenden API-Endpunkt entgegengenommen.
 *
 * @Author Jessica Repty, Tamari Bayer
 */
@Data
@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLink implements Commandable {

    @NotNull
    @Schema(
            description = "The UUID of the link that is to be deleted.",
            required = true,
            example = "dce61f5d-93f8-421d-9552-5567d707b650")
    private UUID id;

    @NotBlank
    @NotNull
    @Schema(
            description = "The name of the link",
            required = true,
            example = "Wichtig")
    private String name;

    @NotNull
    @Schema(
            description = "The Source-Card",
            required = true,
            example = "3b1824120d6d4857843aedfc1973d323")
    private UUID source;

    @NotNull
    @Schema(
            description = "The Destination-Card",
            required = true,
            example = "3b1824120d6d4857843aedfc1973d323")
    private UUID target;
}
