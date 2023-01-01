package io.michaelarnold.zettel.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Builder
@Data
public class ZettelFile {

    @NotBlank
    private String zettelId;

    @NotBlank
    private String content;
}
