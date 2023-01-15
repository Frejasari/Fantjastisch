package de.fantjastisch.cards_backend.learningsystem.aggregate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class CreateLearningSystem {

    @Schema(
            description = "The learning system's label",
            required = true,
            example = "4-Boxen-System")
    private String label;

    @Schema(
            description = "An array of Strings representing box labels.",
            required = true,
            example = "[schlecht, mittel, gut, fantjastisch]")
    private String[] boxLabels;
}
