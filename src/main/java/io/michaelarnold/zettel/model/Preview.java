package io.michaelarnold.zettel.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;
import java.util.List;

@Data
@EqualsAndHashCode
@Builder
public class Preview {

    @PositiveOrZero
    private int id;

    @NotEmpty
    private List<String> attributes;

    @NotBlank
    private String content;

    @NotBlank
    private String zetId;
}
