package de.fantjastisch.cards_backend.learningsystem.aggregate;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class CreateLearningSystem {

    @ApiModelProperty(
            value = "The learning system's label",
            required = true,
            example = "4-Boxen-System")
    private String label;

    @ApiModelProperty(
            value = "A List of Strings representing box labels.",
            required = true,
            example = "[schlecht, mittel, gut, fantjastisch]")
    private List<String> boxLabels;
}
