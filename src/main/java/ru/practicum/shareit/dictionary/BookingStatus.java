package ru.practicum.shareit.dictionary;

public enum BookingStatus {
    WAITING("новое бронирование"),
    APPROVED("бронирование подтверждено владельцем"),
    REJECTED("бронирование отклонено владельцем"),
    CANCELED("бронирование отменено создателем");

    private final String label;
    BookingStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}