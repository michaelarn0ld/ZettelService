package io.michaelarnold.zettel.controller;

import io.michaelarnold.zettel.config.ApplicationConfiguration;
import io.michaelarnold.zettel.domain.ZettelFileService;
import io.michaelarnold.zettel.exceptions.PreviewAWSFetchingException;
import io.michaelarnold.zettel.exceptions.ZettelWhitelistException;
import io.michaelarnold.zettel.model.ZettelFile;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@Log4j2
@RestController
public class ZettelFileController {

    @Autowired
    ZettelFileService service;

    @CrossOrigin
    @GetMapping(ApplicationConfiguration.ZETTEL_FILE_ENDPOINT)
    public ResponseEntity<?> getZettelFile(@PathVariable String zettelId) {
        try {
            ZettelFile zettelFile = service.getZettel(zettelId);
            return new ResponseEntity<>(zettelFile, HttpStatus.OK);
        } catch (ZettelWhitelistException e) {
            log.error(e.getMessage());
        } catch (PreviewAWSFetchingException e) {
            log.error("Server error during request with: " + e.getMessage());
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
