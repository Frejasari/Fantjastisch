package de.fantjastisch.cards_backend.learningsystem.aggregate;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Data
@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLearningSystem {

    @ApiModelProperty(
            value = "The UUID of the learning system that is to be updated.",
            required = true,
            example = "dce61f5d-93f8-421d-9552-5567d707b650"
    )
    private UUID id;

    @ApiModelProperty(
            value = "The learning system's label",
            required = true,
            example = "4-boxen-system")
    private String label;

    @ApiModelProperty(
            value = "An array of child-category UUIDs.",
            required = true,
            example = "[schlecht, mittel, gut, fantjastisch]")
    private List<String> boxLabels;
}
