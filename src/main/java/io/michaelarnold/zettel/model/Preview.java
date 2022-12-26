package io.michaelarnold.zettel.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Preview preview = (Preview) o;
        return Objects.equals(zetId, preview.zetId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(zetId);
    }
}
