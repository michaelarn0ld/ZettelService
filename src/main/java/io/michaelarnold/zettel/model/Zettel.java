package io.michaelarnold.zettel.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Zettel {

    private String zettel_id;

    private String title;

}
