package de.fantjastisch.cards_backend.learningsystem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LearningSystem {
    UUID id;
    String label;
    String[] boxLabels;
}
