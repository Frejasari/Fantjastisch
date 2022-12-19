package de.fantjastisch.cards_backend.category.controller;

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
public class CreateCategory {

  @ApiModelProperty(
      value = "The category label",
      required = true,
      example = "Category #1")
  private String label;
}
