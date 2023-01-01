package io.michaelarnold.zettel.data;

import io.michaelarnold.zettel.model.ZettelFile;

public interface ZettelFileRepository {
    ZettelFile getZettel(String zettelId);
}
