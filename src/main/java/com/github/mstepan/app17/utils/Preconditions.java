package com.github.mstepan.app17.utils;

public final class Preconditions {

    private Preconditions() {
        throw new AssertionError("Can't instantiate utility-only class");
    }

    public static <T> T checkNotNull(T obj, String errorMessage) {
        if (obj == null) {
            throw new IllegalArgumentException(errorMessage);
        }
        return obj;
    }

    public static <T> T checkNotNull(T obj) {
        return checkNotNull(obj, "null value detected");
    }

    public static void checkArguments(boolean condition, String errorMessage) {
        if (!condition) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static void checkState(boolean condition, String errorMessage) {
        if (!condition) {
            throw new IllegalStateException(errorMessage);
        }
    }

    public static void checkInRange(int index, int from, int to, String errorPrefix) {
        if (index < from || index > to) {
            throw new IllegalArgumentException(
                    "%s, expected range: [%d..%d], but found %d"
                            .formatted(errorPrefix, from, to, index));
        }
    }
}
