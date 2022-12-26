package io.michaelarnold.zettel.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Tag {

    private String tag;

    private List<Zettel> zettels;
}
