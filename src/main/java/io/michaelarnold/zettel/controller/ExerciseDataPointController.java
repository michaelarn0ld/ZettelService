package io.michaelarnold.zettel.controller;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import io.michaelarnold.zettel.config.ApplicationConfiguration;
import io.michaelarnold.zettel.domain.ExerciseDataPointService;
import io.michaelarnold.zettel.domain.Result;
import io.michaelarnold.zettel.model.ExerciseDataPoint;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static io.michaelarnold.zettel.config.ApplicationConfiguration.HEALTH_TRACKER_POST_KEY;


@RestController
@Log4j2
public class ExerciseDataPointController {

    @Autowired
    ExerciseDataPointService service;

    @Autowired
    AWSSecretsManager awsSecretsManager;

    @PostMapping(ApplicationConfiguration.WORKOUT_POST_ENDPOINT)
    public ResponseEntity<?> addExerciseDataPoint(@RequestBody @Valid ExerciseDataPoint exerciseDataPoint,
                                                  @RequestParam(name = HEALTH_TRACKER_POST_KEY) String accessPin) {

        GetSecretValueRequest request = new GetSecretValueRequest();
        request.setSecretId(HEALTH_TRACKER_POST_KEY);
        GetSecretValueResult secretResult = awsSecretsManager.getSecretValue(request);
        String secret = secretResult.getSecretString();
        JSONObject object = new JSONObject(secret);
        String key = object.getString(HEALTH_TRACKER_POST_KEY);
        log.info("Fetched key from SecretsManager");

        if (!key.equals(accessPin)){
            log.error("Invalid accessPin from request");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Result<ExerciseDataPoint> result = service.add(exerciseDataPoint);
        if (!result.isSuccess()) {
            log.error("Invalid request submitted");
            return new ResponseEntity<>(result.getMessages(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
    }

}
