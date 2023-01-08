package de.fantjastisch.cards_backend.learningsystem.aggregate;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class DeleteLearningSystem {

    @ApiModelProperty(
            value = "The UUID of the learning system that is to be deleted.",
            required = true,
            example = "dce61f5d-93f8-421d-9552-5567d707b650")
    private UUID id;
}
