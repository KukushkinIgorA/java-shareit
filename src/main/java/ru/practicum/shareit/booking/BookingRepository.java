package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.dictionary.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findByBooker_IdAndItem_Id(int bookerId, int itemId);

    List<Booking> findByItem_IdAndItem_Owner_IdOrderByStart(int itemId, int userId);

    //findUserBooking
    List<Booking> findByBooker_IdOrderByStartDesc(int userId, Pageable pageable);

    List<Booking> findByBooker_IdAndStatusOrderByStartDesc(int userId, BookingStatus bookingStatus, Pageable pageable);

    List<Booking> findByBooker_IdAndEndBeforeOrderByStartDesc(int userId, LocalDateTime now, Pageable pageable);

    List<Booking> findByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(int userId, LocalDateTime nowStart, LocalDateTime nowEnd, Pageable pageable);

    List<Booking> findByBooker_IdAndStartAfterOrderByStartDesc(int userId, LocalDateTime now, Pageable pageable);


    //findItemUserBooking
    List<Booking> findByItem_Owner_IdOrderByStartDesc(int userId, Pageable pageable);

    List<Booking> findByItem_Owner_IdAndStatusOrderByStartDesc(int userId, BookingStatus bookingStatus, Pageable pageable);

    List<Booking> findByItem_Owner_IdAndEndBeforeOrderByStartDesc(int userId, LocalDateTime now, Pageable pageable);

    List<Booking> findByItem_Owner_IdAndStartBeforeAndEndAfterOrderByStartDesc(int userId, LocalDateTime nowStart, LocalDateTime nowEnd, Pageable pageable);

    List<Booking> findByItem_Owner_IdAndStartAfterOrderByStartDesc(int userId, LocalDateTime now, Pageable pageable);
}