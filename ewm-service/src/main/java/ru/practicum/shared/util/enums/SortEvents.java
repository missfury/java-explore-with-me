package ru.practicum.shared.util.enums;

import java.util.Arrays;
import java.util.Optional;

public enum SortEvents {
    EVENT_DATE, VIEWS;

    public static Optional<SortEvents> from(String stringState) {
        return Arrays.stream(values())
                .filter(state -> state.name().equalsIgnoreCase(stringState))
                .findFirst();
    }
}
