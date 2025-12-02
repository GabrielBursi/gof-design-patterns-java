package org.branas.io;

import org.jetbrains.annotations.NotNull;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CancelBookingUseCase implements UseCase<CancelBookingInput, Void> {
    private final BookingRepository bookingRepository;

    @Override
    public Void execute(@NotNull CancelBookingInput input) {
        var booking = bookingRepository.findByCode(input.code());

        if (booking == null) {
            throw new RuntimeException("Booking not found");
        }

        booking.cancel();
        bookingRepository.update(booking);

        return null;
    }
}
