package io.michaelarnold.zettel.data;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.google.gson.Gson;
import io.michaelarnold.zettel.config.ApplicationConfiguration;
import io.michaelarnold.zettel.model.Mappings;
import io.michaelarnold.zettel.model.Preview;
import io.michaelarnold.zettel.model.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Log4j2
public class PreviewS3Repository implements PreviewRepository {

    private static final String ZETTEL_PREFIX = "STARTTITLE_";
    private static final String ZETTEL_SUFFIX = "_ENDTITLE";
    private static final String ZETTEL_TITLE_CONVENTION = "# ";

    @Autowired
    private AmazonS3 amazonS3;

    @Override
    public List<Preview> getPreviews() {
        S3Object s3Object = amazonS3.getObject(
                new GetObjectRequest(ApplicationConfiguration.BUCKET, ApplicationConfiguration.KEY));
        String jsonContent = null;
        try {
            jsonContent = convertS3ToString(s3Object.getObjectContent());
        } catch (Exception e) {
            // TODO: Handle exception
        }
        Gson gson = new Gson();
        Mappings mappings = gson.fromJson(jsonContent, Mappings.class);
        Map<String, Preview> previewMap = new HashMap<>();
        for (Tag tag: mappings.getTags()) {
            tag.getZettels().forEach(zettel -> {
                if (!previewMap.containsKey(zettel.getZettel_id())) {
                    Preview zettelPreview = Preview.builder()
                            .id(0) // we will set the ID in the service layer based on the allowlist
                            .attributes(new ArrayList<>())
                            .content(parseZettelTitle(zettel.getTitle()))
                            .zetId(zettel.getZettel_id())
                            .build();
                    previewMap.put(zettelPreview.getZetId(), zettelPreview);
                }
                previewMap.get(zettel.getZettel_id()).getAttributes().add(tag.getTag());
            });
        }
        return new ArrayList<>(previewMap.values());
    }
    private String convertS3ToString(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder jsonContent = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            jsonContent.append(line);
        }
        return jsonContent.toString();
    }

    private String parseZettelTitle(String zettelTitle) {
       return zettelTitle.replace(ZETTEL_PREFIX, "")
               .replace(ZETTEL_SUFFIX, "")
               .replace(ZETTEL_TITLE_CONVENTION, "");
    }
}
