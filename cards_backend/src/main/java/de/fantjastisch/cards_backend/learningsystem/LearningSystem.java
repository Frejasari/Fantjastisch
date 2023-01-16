package de.fantjastisch.cards_backend.learningsystem;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LearningSystem {
    @Schema(required = true)
    UUID id;
    @Schema(required = true)
    String label;
    @Schema(required = true)
    List<String> boxLabels;
}
