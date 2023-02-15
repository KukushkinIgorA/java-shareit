package ru.practicum.shareit.dictionary;

import java.util.Optional;

public enum BookingGetState {
    ALL("все"),
    CURRENT("текущие"),
    PAST("завершённые"),
    FUTURE("будущие"),
    WAITING("ожидающие подтверждения"),
    REJECTED("отклонённые");

    private final String label;

    BookingGetState(String label) {
        this.label = label;
    }

    public static Optional<BookingGetState> from(String bookingGetStateString) {
        for (BookingGetState value : BookingGetState.values()) {
            if (value.name().equals(bookingGetStateString)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }

    public String getLabel() {
        return label;
    }
}