package de.fantjastisch.cards_backend.link.aggregate;

import de.fantjastisch.cards_backend.util.validation.Commandable;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CreateLink implements Commandable {

    @NotBlank
    @NotNull
    @ApiModelProperty(
            value = "The name of the link",
            required = true,
            example = "Wichtig")
    private String name;

    @NotNull
    @ApiModelProperty(
            value = "The Source-Card",
            required = true,
            example = "3b1824120d6d4857843aedfc1973d323")
    private UUID source;

    @NotNull
    @ApiModelProperty(
            value = "The Destination-Card",
            required = true,
            example = "3b1824120d6d4857843aedfc1973d323")
    private UUID target;


}
