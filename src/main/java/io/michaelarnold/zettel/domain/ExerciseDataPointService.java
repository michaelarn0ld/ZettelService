package io.michaelarnold.zettel.domain;

import io.michaelarnold.zettel.data.ExerciseDataPointRepository;
import io.michaelarnold.zettel.model.ExerciseDataPoint;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

@Service
@Log4j2
public class ExerciseDataPointService {

    @Autowired
    ExerciseDataPointRepository repository;

    @Autowired
    Validator validator;

    public Result<ExerciseDataPoint> add(ExerciseDataPoint exerciseDataPoint) {
        Result<ExerciseDataPoint> result = new Result<>();
        Set<ConstraintViolation<ExerciseDataPoint>> violations = validator.validate(exerciseDataPoint);
        log.info("Checking ExerciseDataPoint validity of: " + exerciseDataPoint);
        if (!violations.isEmpty()) {
            violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(result::addErrorMessage);
            log.error("Invalid ExerciseDataPoint with errors: " + result.getMessages());
            return result;
        }
        log.info("Valid ExerciseDataPoint; attempting to add to DynamoDB");
        result.setPayload(repository.add(exerciseDataPoint));
        return result;
    }

}
