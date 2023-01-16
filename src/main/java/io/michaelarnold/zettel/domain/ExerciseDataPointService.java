package io.michaelarnold.zettel.domain;

import io.michaelarnold.zettel.data.ExerciseDataPointRepository;
import io.michaelarnold.zettel.model.ExerciseDataPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

@Service
public class ExerciseDataPointService {

    @Autowired
    ExerciseDataPointRepository repository;

    @Autowired
    Validator validator;

    public Result<ExerciseDataPoint> add(ExerciseDataPoint exerciseDataPoint) {
        Result<ExerciseDataPoint> result = new Result<>();
        Set<ConstraintViolation<ExerciseDataPoint>> violations = validator.validate(exerciseDataPoint);
        if (!violations.isEmpty()) {
            violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(result::addErrorMessage);
            return result;
        }
        result.setPayload(repository.add(exerciseDataPoint));
        return result;
    }

}
