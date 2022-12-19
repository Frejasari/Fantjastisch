package de.fantjastisch.cards_backend.card.controller;

import io.swagger.annotations.ApiModelProperty;
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
public class CreateCard {

  @ApiModelProperty(
      value = "The question to be asked",
      required = true,
      example = "Who am I?")
  private String question;

  @ApiModelProperty(
      value = "The answer to the question",
      required = true,
      example = "I am me")
  private String answer;
}
