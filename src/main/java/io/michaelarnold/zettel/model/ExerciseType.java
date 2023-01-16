package io.michaelarnold.zettel.model;

public enum ExerciseType {
    BENCH_PRESS("BENCHPRESS"),
    BACK_SQUAT("BACKSQUAT"),
    FRONT_SQUAT("FRONTSQUAT"),
    OVERHEAD_PRESS("OVERHEADPRESS"),
    BARBELL_ROW("BARBELLROW"),
    DEAD_LIFT("DEADLIFT");

    private final String name;

    @Override
    public String toString() {
        return name;
    }

    ExerciseType(String name) {
        this.name = name;
    }

    public static ExerciseType fromString(String name) throws IllegalAccessException {
        for (ExerciseType e: ExerciseType.values()) {
            if (e.name.equals(name)) return e;
        }
        throw new IllegalAccessException(String.format("No %s can be parsed from: %s", ExerciseType.class.getName(), name));
    }

}
