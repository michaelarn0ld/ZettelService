package io.michaelarnold.zettel.data;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterResult;
import com.google.gson.Gson;
import io.michaelarnold.zettel.config.ApplicationConfiguration;
import io.michaelarnold.zettel.exceptions.PreviewAWSFetchingException;
import io.michaelarnold.zettel.model.Mappings;
import io.michaelarnold.zettel.model.Preview;
import io.michaelarnold.zettel.model.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
    private List<Preview> previews;
    private Mappings mappings;

    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private AWSSimpleSystemsManagement amazonSSM;

    @Override
    public List<Preview> getPreviews() {
        if (mappings != null && previews != null && isMostCurrentMapping(mappings)) {
            log.info("No change in Zettelkasten git head; using cached MAPPINGS");
            return previews;
        }
        log.info("About to get MAPPINGS.json from Amazon S3");
        String jsonContent = null;
        try {
            S3Object s3Object = amazonS3.getObject(
                    new GetObjectRequest(ApplicationConfiguration.MAPPINGS_BUCKET, ApplicationConfiguration.MAPPINGS_KEY));
            jsonContent = RepositoryUtils.convertS3ToString(s3Object.getObjectContent());
        } catch (Exception e) {
            log.error("Error retrieving and/or parsing MAPPINGS.json from Amazon S3 with: " + e.getMessage());
            throw new PreviewAWSFetchingException(e.getMessage());
        }
        Gson gson = new Gson();
        mappings = gson.fromJson(jsonContent, Mappings.class);
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
        previews = new ArrayList<>(previewMap.values());
        return previews;
    }


    private String parseZettelTitle(String zettelTitle) {
       return zettelTitle.replace(ZETTEL_PREFIX, "")
               .replace(ZETTEL_SUFFIX, "")
               .replace(ZETTEL_TITLE_CONVENTION, "");
    }

    private boolean isMostCurrentMapping(Mappings mappings) {
        String latestGitHead = fetchGitHead();
        log.info(String.format("Cached mappings is at commit: %s; latest commit is: %s",
                mappings.getGit_head(), latestGitHead));
        return mappings.getGit_head().equals(latestGitHead);
    }

    private String fetchGitHead() {
        String result = null;
        try {
            GetParameterRequest request = new GetParameterRequest().withName(ApplicationConfiguration.SSM_GIT_HEAD);
            GetParameterResult response = amazonSSM.getParameter(request);
            result = response.getParameter().getValue();
        } catch (Exception e) {
            // If there is a failure to fetch from SSM, then we will go ahead an ask S3 for the MAPPINGS object
            log.error("Failure to query SSM; proceeding to fetch MAPPINGS.json from S3");
        }
        return result;
    }
}
