package org.branas.io;

import org.jetbrains.annotations.NotNull;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BookRoomUseCase implements UseCase<BookRoomInput, BookRoomOutput> {
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;

    @Override
    public BookRoomOutput execute(@NotNull BookRoomInput input) {
        var availableRooms = roomRepository.findAvailableRoomsByPeriodAndCategory(
            input.checkinDate(),
            input.checkoutDate(),
            input.category()
        );

        if (availableRooms.isEmpty()) {
            throw new RuntimeException("Room is not available");
        }

        var availableRoom = availableRooms.get(0);
        var booking = Booking.create(availableRoom, input.email(), input.checkinDate(), input.checkoutDate());
        bookingRepository.save(booking);

        return new BookRoomOutput(booking.getCode());
    }
}

