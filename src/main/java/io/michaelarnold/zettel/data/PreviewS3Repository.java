package io.michaelarnold.zettel.data;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.google.gson.Gson;
import io.michaelarnold.zettel.config.ApplicationConfiguration;
import io.michaelarnold.zettel.exceptions.PreviewAWSFetchingException;
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
import java.util.stream.Collectors;

@Repository
@Log4j2
public class PreviewS3Repository implements PreviewRepository {

    private static final String ZETTEL_PREFIX = "STARTTITLE_";
    private static final String ZETTEL_SUFFIX = "_ENDTITLE";
    private static final String ZETTEL_TITLE_CONVENTION = "# ";

    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    @Override
    public List<Preview> getPreviews() {
        log.info("About to get MAPPINGS.json from Amazon S3");
        String jsonContent = null;
        try {
            S3Object s3Object = amazonS3.getObject(
                    new GetObjectRequest(ApplicationConfiguration.BUCKET, ApplicationConfiguration.KEY));
            jsonContent = convertS3ToString(s3Object.getObjectContent());
        } catch (Exception e) {
            log.error("Error retrieving and/or parsing MAPPINGS.json from Amazon S3 with: " + e.getMessage());
            throw new PreviewAWSFetchingException(e.getMessage());
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
        log.info("Successfully fetched and parsed MAPPINGS.json from Amazon S3");
        return new ArrayList<>(previewMap.values());
    }

    @Override
    public List<String> getWhitelist() {
        log.info("About to fetch zettel whitelist from Amazon DynamoDB");
        Map<String, AttributeValue> key = new HashMap<>();
        key.put(ApplicationConfiguration.WHITELIST_PRIMARY_KEY_NAME,
                new AttributeValue(ApplicationConfiguration.WHITELIST_PRIMARY_KEY_VALUE));
        GetItemRequest request = new GetItemRequest()
                .withKey(key)
                .withTableName(ApplicationConfiguration.WHITELIST_TABLE_NAME);
        AttributeValue whitelistAttribute = null;
        try {
            Map<String, AttributeValue> result = amazonDynamoDB.getItem(request).getItem();
            whitelistAttribute = result.get(ApplicationConfiguration.WHITELIST_VALUES);
        } catch (Exception e) {
            log.error("Error fetching whitelist from DynamoDB with: " + e.getMessage());
            throw new PreviewAWSFetchingException(e.getMessage());
        }
        log.info("Successfully fetched zettel whitelist from Amazon DynamoDB");
        return whitelistAttribute.getL().stream()
                .map(AttributeValue::getS)
                .collect(Collectors.toList());
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
