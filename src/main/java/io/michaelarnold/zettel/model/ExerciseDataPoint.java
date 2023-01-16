package io.michaelarnold.zettel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseDataPoint {

    @Null
    private LocalDateTime logTime;

    @NotNull
    private Float weightLbs;

    @NotNull
    private Float bodyFatPercentage;

    @NotNull
    private ExerciseType exerciseType;

    @NotNull
    private RepRange repRange;

    @NotNull
    private Integer maximumWeightAchieved;
}
