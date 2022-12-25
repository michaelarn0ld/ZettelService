package io.michaelarnold.zettel.controller;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import io.michaelarnold.zettel.domain.PreviewService;
import io.michaelarnold.zettel.model.Preview;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@RestController
@Log4j2
public class PreviewController {

    @Autowired
    PreviewService service;

    @GetMapping("/preview")
    public ResponseEntity<?> getAllPreviews() throws IOException {
        log.info("Getting zettel previews");
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.US_WEST_1)
                .withCredentials(new ProfileCredentialsProvider())
                .build();
        log.info("Downloading an S3 object");
        String bucketName = "zettel-tag-mappings";
        String key = "MAPPINGS.json";
        S3Object object = s3Client.getObject(new GetObjectRequest(bucketName, key));
        displayTextInputStream(object.getObjectContent());
        return null;
    }

    @GetMapping("/previews")
    public ResponseEntity<?> getPreviews() {
        List<Preview> previews = service.getPreviews();
        return new ResponseEntity<>(previews, HttpStatus.OK);
    }

    private void displayTextInputStream(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        while ((line = reader.readLine()) != null) {
            log.info(line);
        }
    }
}
