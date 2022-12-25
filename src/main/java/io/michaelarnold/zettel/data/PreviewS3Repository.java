package io.michaelarnold.zettel.data;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import io.michaelarnold.zettel.config.ApplicationConfiguration;
import io.michaelarnold.zettel.model.Preview;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Repository
@Log4j2
public class PreviewS3Repository implements PreviewRepository {

    @Autowired
    private AmazonS3 amazonS3;

    @Override
    public List<Preview> getPreviews() {
        S3Object s3Object = amazonS3.getObject(
                new GetObjectRequest(ApplicationConfiguration.BUCKET, ApplicationConfiguration.KEY));
        // TODO: get the "tags" attribute of the original and make a List<JsonObj> so that we can perform the transform operations
        return null;
    }
    private void displayTextInputStream(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        while ((line = reader.readLine()) != null) {
            log.info(line);
        }
    }
}
