package io.michaelarnold.zettel.controller;

import io.michaelarnold.zettel.config.ApplicationConfiguration;
import io.michaelarnold.zettel.domain.PreviewService;
import io.michaelarnold.zettel.exceptions.PreviewAWSFetchingException;
import io.michaelarnold.zettel.model.Preview;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Log4j2
public class PreviewController {

    @Autowired
    PreviewService service;

    @GetMapping(ApplicationConfiguration.PREVIEWS_ENDPOINT)
    public ResponseEntity<?> getPreviews() {
        try {
            List<Preview> previews = service.getPreviews();
            return new ResponseEntity<>(previews, HttpStatus.OK);
        } catch (PreviewAWSFetchingException e) {
            log.error("Server error during request with: " + e.getMessage());
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
