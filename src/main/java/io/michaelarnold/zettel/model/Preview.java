package io.michaelarnold.zettel.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Data
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
