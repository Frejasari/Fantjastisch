package de.fantjastisch.cards_backend.util.validation.errors;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.ConstraintViolation;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@SuperBuilder
public class ErrorEntry {

    @ApiModelProperty(example = "1002", required = true)
    private ErrorCode code;

    @ApiModelProperty(example = "name", required = true)
    private String field;

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private transient ConstraintViolation violation;

    public static String mapErrorsToString(List<ErrorEntry> errors) {
        List<String> toStrings = errors.stream().map(x -> String.format("Error code: %s on field %s", x.getCode(), x.getField())).collect(Collectors.toList());
        return String.join("| ", toStrings);
    }
}

