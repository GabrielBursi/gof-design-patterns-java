package org.branas.io;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BookRoomUseCaseTest {
    private RoomRepository roomRepository;
    private BookingRepository bookingRepository;

    @BeforeEach
    void setUp() {
        roomRepository = new RoomRepositoryDatabase();
        bookingRepository = new BookingRepositoryDatabase();
    }

    @Test
    @DisplayName("should throw exception when room is not available")
    void shouldThrowExceptionWhenRoomNotAvailable() {
        BookRoomUseCase bookRoom = new BookRoomUseCase(roomRepository, bookingRepository);
        BookRoomInput input = new BookRoomInput(
            "test@example.com",
            LocalDateTime.of(2021, 3, 1, 10, 0),
            LocalDateTime.of(2021, 3, 5, 10, 0),
            "suite"
        );

        assertThrows(RuntimeException.class, () -> bookRoom.execute(input));
    }
}
