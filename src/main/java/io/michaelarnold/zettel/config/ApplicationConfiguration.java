package io.michaelarnold.zettel.config;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    public static final String BUCKET = "zettel-tag-mappings";
    public static final String KEY = "MAPPINGS.json";
    public static final String WHITELIST_PRIMARY_KEY_NAME = "whitelist";
    public static final String WHITELIST_PRIMARY_KEY_VALUE = "PRIMARY";
    public static final String WHITELIST_TABLE_NAME = "ZettelWhitelist";
    public static final String WHITELIST_VALUES = "values";
    private static final Regions REGION = Regions.US_WEST_1;
    private static final ProfileCredentialsProvider CREDENTIALS = new ProfileCredentialsProvider();

    @Bean
    public AmazonS3 inititializeAmazonS3() {
       return AmazonS3ClientBuilder.standard()
                .withRegion(REGION)
                .withCredentials(CREDENTIALS)
                .build();
    }

    @Bean
    public AmazonDynamoDB initializeAmazonDynamoDB() {
        return AmazonDynamoDBClientBuilder.standard()
                .withRegion(REGION)
                .withCredentials(CREDENTIALS)
                .build();
    }


}
