package ru.practicum.shareit.dictionary;

import javax.persistence.AttributeConverter;

public class BookingStatusConverter implements AttributeConverter<BookingStatus, String> {
    @Override
    public String convertToDatabaseColumn(BookingStatus bookingStatus) {
        if (bookingStatus == null)
            return null;

        switch (bookingStatus) {
            case WAITING:
                return "1";

            case APPROVED:
                return "2";

            case REJECTED:
                return "3";

            case CANCELED:
                return "4";

            default:
                throw new IllegalArgumentException(bookingStatus + "not supported");
        }
    }

    @Override
    public BookingStatus convertToEntityAttribute(String dbData) {
        if (dbData == null)
            return null;

        switch (dbData) {
            case "1":
                return BookingStatus.WAITING;

            case "2":
                return BookingStatus.APPROVED;

            case "3":
                return BookingStatus.REJECTED;

            case "4":
                return BookingStatus.CANCELED;

            default:
                throw new IllegalArgumentException(dbData + "not supported");

        }
    }
}
