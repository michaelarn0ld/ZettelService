package io.michaelarnold.zettel.data;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import io.michaelarnold.zettel.config.ApplicationConfiguration;
import io.michaelarnold.zettel.exceptions.PreviewAWSFetchingException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
public class RepositoryUtils {

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    public static String convertS3ToString(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder content = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            content.append(line);
            content.append("\n");
        }
        return content.toString();
    }

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

}
