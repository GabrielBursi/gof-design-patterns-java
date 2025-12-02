package org.branas.io;

import org.jetbrains.annotations.NotNull;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetBookingByCode implements UseCase<GetBookingByCodeInput, GetBookingByCodeOutput> {
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;

    @Override
    public GetBookingByCodeOutput execute(@NotNull GetBookingByCodeInput input) {
        var booking = bookingRepository.findByCode(input.code());

        if (booking == null) {
            throw new RuntimeException("Booking not found");
        }

        var room = roomRepository.findById(booking.getRoomId());

        return new GetBookingByCodeOutput(booking.getCode(), room.getCategory(), booking.getDuration(), booking.getPrice());
    }
}
