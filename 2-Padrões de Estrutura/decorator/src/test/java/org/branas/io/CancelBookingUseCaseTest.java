package org.branas.io;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CancelBookingUseCaseTest {

    @Test
    @DisplayName("should throw exception when booking not found")
    void shouldThrowExceptionWhenBookingNotFound() {
        BookingRepository bookingRepository = new BookingRepositoryDatabase();
        CancelBookingUseCase cancelBooking = new CancelBookingUseCase(bookingRepository);

        CancelBookingInput input = new CancelBookingInput("invalid-code");

        assertThrows(RuntimeException.class, () -> cancelBooking.execute(input));
    }
}
