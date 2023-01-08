package de.fantjastisch.cards_backend.card.aggregate;

import de.fantjastisch.cards_backend.util.validation.Commandable;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Map;
import java.util.UUID;

@Data
@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCard implements Commandable {

    @NotNull
    @ApiModelProperty(
            value = "The UUID of the card that is to be updated.",
            required = true,
            example = "dce61f5d-93f8-421d-9552-5567d707b650")
    private UUID id;

    @NotNull
    @NotBlank
    @ApiModelProperty(
            value = "The question to be asked",
            required = true,
            example = "Who am I?")
    private String question;

    @NotNull
    @NotBlank
    @ApiModelProperty(
            value = "The answer to the question",
            required = true,
            example = "I am me")
    private String answer;

    @ApiModelProperty(
            value = "Tag",
            example = "I am a tag")
    private String tag;

    @NotNull
    @NotEmpty
    @ApiModelProperty(
            value = "Zugehörige Kategorien",
            required = true,
            example = "[3b1824120d6d4857843aedfc1973d323, 40ac4fcc97024a87b0bdbffe1f7f49f8]")
    private UUID[] categories;
}