package ru.practicum.shareit.dictionary;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookingStatusConverterTest {

    BookingStatusConverter bookingStatusConverter;

    @BeforeEach
    void init() {
        bookingStatusConverter = new BookingStatusConverter();
    }

    @ParameterizedTest()
    @EnumSource(value = BookingStatus.class, names = {"WAITING", "APPROVED", "REJECTED", "CANCELED"})
    void convertToDatabaseColumn(BookingStatus bookingStatus) {
        switch (bookingStatus) {
            case WAITING:
                assertEquals("1", bookingStatusConverter.convertToDatabaseColumn(bookingStatus));
                break;
            case APPROVED:
                assertEquals("2", bookingStatusConverter.convertToDatabaseColumn(bookingStatus));
                break;
            case REJECTED:
                assertEquals("3", bookingStatusConverter.convertToDatabaseColumn(bookingStatus));
                break;
            case CANCELED:
                assertEquals("4", bookingStatusConverter.convertToDatabaseColumn(bookingStatus));
                break;
        }

    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "2", "3", "4"})
    void convertToEntityAttribute(String dbData) {
        switch (dbData) {
            case "1":
                assertEquals(BookingStatus.WAITING, bookingStatusConverter.convertToEntityAttribute(dbData));
                break;
            case "2":
                assertEquals(BookingStatus.APPROVED, bookingStatusConverter.convertToEntityAttribute(dbData));
                break;
            case "3":
                assertEquals(BookingStatus.REJECTED, bookingStatusConverter.convertToEntityAttribute(dbData));
                break;
            case "4":
                assertEquals(BookingStatus.CANCELED, bookingStatusConverter.convertToEntityAttribute(dbData));
                break;
        }
    }
}