package io.michaelarnold.zettel.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/preview")
@Log4j2
public class PreviewController {

    @GetMapping
    public ResponseEntity<?> getPreviews() {
        log.info("Getting zettel previews");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
