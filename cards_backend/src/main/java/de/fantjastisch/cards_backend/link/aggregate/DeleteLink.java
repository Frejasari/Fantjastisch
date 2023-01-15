package de.fantjastisch.cards_backend.link.aggregate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/**
 * Diese Klasse stellt ein CRUD-Kommando zum Löschen einer Link-Entität dar.
 * Eine Instanz dieser Klasse wird als Parameter vom entsprechenden API-Endpunkt entgegengenommen.
 *
 * @Author Jessica Repty, Tamari Bayer
 */
@Data
@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class DeleteLink {

    @NotNull
    @Schema(
            description = "The UUID of the link that is to be deleted.",
            required = true,
            example = "dce61f5d-93f8-421d-9552-5567d707b650")
    private UUID id;
}
