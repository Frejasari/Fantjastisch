package de.fantjastisch.cards_backend.learningsystem.aggregate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/**
 * Diese Klasse stellt ein CRUD-Kommando zum Löschen einer Lernsystem-Entität dar.
 * Eine Instanz dieser Klasse wird als Parameter vom entsprechenden API-Endpunkt entgegengenommen.
 *
 * @author Semjon Nirmann, Jessica Repty, Alex Kück
 */
@Data
@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class DeleteLearningSystem {

    @NotNull
    @Schema(
            description = "The UUID of the learning system that is to be deleted.",
            required = true,
            example = "dce61f5d-93f8-421d-9552-5567d707b650")
    private UUID id;
}
