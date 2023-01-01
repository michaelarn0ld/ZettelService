package io.michaelarnold.zettel.data;

import io.michaelarnold.zettel.model.Preview;

import java.util.List;

public interface PreviewRepository {
    List<Preview> getPreviews();
}
