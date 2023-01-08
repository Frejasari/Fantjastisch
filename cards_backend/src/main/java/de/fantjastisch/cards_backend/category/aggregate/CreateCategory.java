package de.fantjastisch.cards_backend.category.aggregate;

import de.fantjastisch.cards_backend.util.validation.Commandable;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CreateCategory implements Commandable {

    @NotBlank
    @NotNull
    @ApiModelProperty(
            value = "The category label",
            required = true,
            example = "Category #1")
    private String label;

    @ApiModelProperty(
            value = "An array of child-category UUIDs.",
            required = false,
            example = "[fa9d9f26-8e58-4653-809c-c3b5d8e7d97f]")
    private List<UUID> subCategories;
}
