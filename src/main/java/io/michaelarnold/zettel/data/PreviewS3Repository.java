package io.michaelarnold.zettel.data;

import io.michaelarnold.zettel.model.Preview;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Log4j2
public class PreviewS3Repository implements PreviewRepository {

    @Override
    public List<Preview> getPreviews() {
//        Preview preview = Preview.builder()
//                .id(1)
//                .zetId("FOO")
//                .attributes(List.of("bar", "barfoo"))
//                .content("This is the content")
//                .build();
//        return List.of(preview);
        Preview preview = Preview.builder().build();
        List<Preview> list = new ArrayList<>();
        list.add(preview);
        return list;
    }
}
