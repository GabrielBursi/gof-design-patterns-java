package org.branas.io;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GetBookingByCodeTest {

    @Test
    @DisplayName("should throw exception when booking not found")
    void shouldThrowExceptionWhenBookingNotFound() {
        RoomRepository roomRepository = new RoomRepositoryDatabase();
        BookingRepository bookingRepository = new BookingRepositoryDatabase();
        GetBookingByCode getBookingByCode = new GetBookingByCode(roomRepository, bookingRepository);

        GetBookingByCodeInput input = new GetBookingByCodeInput("invalid-code");

        assertThrows(RuntimeException.class, () -> getBookingByCode.execute(input));
    }
}
