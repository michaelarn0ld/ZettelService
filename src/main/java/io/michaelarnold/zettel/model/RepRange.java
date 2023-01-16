package io.michaelarnold.zettel.model;

public enum RepRange {

    THREE_REPS(3),
    FIVE_REPS(5),
    TEN_REPS(10),
    TWENTY_REPS(20);

    private final int reps;

    @Override
    public String toString() {
        return Integer.toString(reps);
    }

    RepRange(int reps) {
        this.reps = reps;
    }

    public static RepRange fromString(int reps) throws IllegalAccessException {
        for (RepRange r: RepRange.values()) {
            if (r.reps == reps) return r;
        }
        throw new IllegalAccessException(String.format("No %s can be parsed from: %d", RepRange.class.getName(), reps));
    }

}
