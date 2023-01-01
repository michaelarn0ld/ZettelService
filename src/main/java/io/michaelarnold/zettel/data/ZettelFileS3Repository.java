package io.michaelarnold.zettel.data;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import io.michaelarnold.zettel.config.ApplicationConfiguration;
import io.michaelarnold.zettel.exceptions.PreviewAWSFetchingException;
import io.michaelarnold.zettel.model.ZettelFile;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Log4j2
public class ZettelFileS3Repository implements ZettelFileRepository {

    @Autowired
    private AmazonS3 amazonS3;

    @Override
    public ZettelFile getZettel(String zettelId) {
        String markdown = null;
        try {
            S3Object s3Object = amazonS3.getObject(
                    new GetObjectRequest(ApplicationConfiguration.ZETTEL_BUCKET, ApplicationConfiguration.zettelKey(zettelId))
            );
            markdown = RepositoryUtils.convertS3ToString(s3Object.getObjectContent());
        } catch (Exception e) {
            log.error("Error retrieving and/or parsing Zettel(id="+ zettelId +") from Amazon S3 with: " + e.getMessage());
            throw new PreviewAWSFetchingException(e.getMessage());
        }
        return ZettelFile.builder()
                .content(markdown)
                .zettelId(zettelId)
                .build();
    }
}
