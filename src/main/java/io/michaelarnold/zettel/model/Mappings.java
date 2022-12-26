package io.michaelarnold.zettel.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Mappings {

    private String git_head;

    private List<Tag> tags;
}
