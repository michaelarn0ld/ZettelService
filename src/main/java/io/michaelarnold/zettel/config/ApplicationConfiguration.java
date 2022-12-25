package io.michaelarnold.zettel.config;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
public class ApplicationConfiguration {

    public static final String BUCKET = "zettel-tag-mappings";
    public static final String KEY = "MAPPINGS.json";
    @Bean
    public AmazonS3 inititializeAmazonS3() {
       return AmazonS3ClientBuilder.standard()
                .withRegion(Regions.US_WEST_1)
                .withCredentials(new ProfileCredentialsProvider())
                .build();
    }
}
