package org.branas.io;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        RoomRepository roomRepository = new RoomRepositoryDatabase();
        BookingRepository bookingRepository = new BookingRepositoryDatabase();

        BookRoomUseCase bookRoom = new BookRoomUseCase(roomRepository, bookingRepository);
        UseCase<BookRoomInput, BookRoomOutput> decoratedUseCase = 
            new SecurityDecorator<>(new LogDecorator<>(bookRoom));

        BookRoomInput input = new BookRoomInput(
            "test@example.com",
            LocalDateTime.of(2024, 3, 1, 10, 0),
            LocalDateTime.of(2024, 3, 5, 10, 0),
            "suite"
        );

        try {
            BookRoomOutput output = decoratedUseCase.execute(input);
            System.out.println("Booking code: " + output.code());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
