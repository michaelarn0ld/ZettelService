package io.michaelarnold.zettel.config;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClient;
import io.michaelarnold.zettel.data.RepositoryUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;

@Configuration
public class ApplicationConfiguration {

    public static final String MAPPINGS_BUCKET = "zettel-tag-mappings";
    public static final String MAPPINGS_KEY = "MAPPINGS.json";
    public static final String ZETTEL_BUCKET = "zettels-repository";
    public static final String WHITELIST_PRIMARY_KEY_NAME = "whitelist";
    public static final String WHITELIST_PRIMARY_KEY_VALUE = "PRIMARY";
    public static final String WHITELIST_TABLE_NAME = "ZettelWhitelist";
    public static final String WHITELIST_VALUES = "zettelValues";
    public static final String PREVIEWS_ENDPOINT = "/previews";
    public static final String ZETTEL_FILE_ENDPOINT="/zettels/{zettelId}";
    public static final String SSM_GIT_HEAD = "git_head";
    public static final String HEALTH_TRACKER_POST_KEY = "accessPin";
    public static final String HEALTH_TRACKER_TABLE_NAME = "HealthTracker";
    public static final String WORKOUT_POST_ENDPOINT = "/exerciseDataPoint";
    private static final Regions REGION = Regions.US_WEST_1;
    private static final ProfileCredentialsProvider CREDENTIALS = new ProfileCredentialsProvider();

    public static String zettelKey(String zettelId) {
        return zettelId + ".md";
    }

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

    @Bean
    public AWSSimpleSystemsManagement initializeAmazonSSM() {
        return AWSSimpleSystemsManagementClient.builder()
                .withRegion(REGION)
                .withCredentials(CREDENTIALS)
                .build();
    }

    @Bean
    public RepositoryUtils initializeRepositoryUtils() {
        return new RepositoryUtils();
    }

    @Bean
    public AWSSecretsManager initializeAmazonSecretsManager() {
        return AWSSecretsManagerClientBuilder.standard()
                .withRegion(REGION)
                .withCredentials(CREDENTIALS)
                .build();
    }

    @Bean
    public Validator initializeValidator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }

}
