package org.branas.io;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RideTest {

    // Rates: Normal=2.10 | Overnight=3.90 | Sunday=2.90 | OvernightSunday=5.00
    // Minimum fare: 10.00

    // --- Tracer bullet ---

    @Test
    void normalWeekdayDaytimeSegmentBelowMinimumAppliesMinimumFare() {
        // Monday 2024-03-04 10:00 — weekday, daytime → 1km × 2.10 = 2.10 < 10 → 10.00
        var ride = new Ride();
        ride.addSegment(1, LocalDateTime.of(2024, 3, 4, 10, 0));
        ride.calculateFare();
        assertEquals(10.00, ride.getFare(), 0.001);
    }

    // --- Normal fare ---

    @Test
    void normalWeekdayDaytimeSegmentsAccumulateAboveMinimum() {
        // 6km × 2.10 = 12.60
        var ride = new Ride();
        ride.addSegment(6, LocalDateTime.of(2024, 3, 4, 10, 0));
        ride.calculateFare();
        assertEquals(12.60, ride.getFare(), 0.001);
    }

    // --- Overnight fare ---

    @Test
    void overnightSegmentUsesOvernightRate() {
        // Monday 22:00 — overnight → 4km × 3.90 = 15.60
        var ride = new Ride();
        ride.addSegment(4, LocalDateTime.of(2024, 3, 4, 22, 0));
        ride.calculateFare();
        assertEquals(15.60, ride.getFare(), 0.001);
    }

    @Test
    void earlyMorningSegmentIsAlsoOvernight() {
        // Monday 05:00 — still overnight → 4km × 3.90 = 15.60
        var ride = new Ride();
        ride.addSegment(4, LocalDateTime.of(2024, 3, 4, 5, 0));
        ride.calculateFare();
        assertEquals(15.60, ride.getFare(), 0.001);
    }

    // --- Sunday fare ---

    @Test
    void sundayDaytimeSegmentUsesSundayRate() {
        // Sunday 2024-03-03 10:00 — sunday daytime → 4km × 2.90 = 11.60
        var ride = new Ride();
        ride.addSegment(4, LocalDateTime.of(2024, 3, 3, 10, 0));
        ride.calculateFare();
        assertEquals(11.60, ride.getFare(), 0.001);
    }

    // --- Overnight Sunday fare ---

    @Test
    void sundayOvernightSegmentUsesHighestRate() {
        // Sunday 22:00 → 3km × 5.00 = 15.00
        var ride = new Ride();
        ride.addSegment(3, LocalDateTime.of(2024, 3, 3, 22, 0));
        ride.calculateFare();
        assertEquals(15.00, ride.getFare(), 0.001);
    }

    // --- Mixed segments ---

    @Test
    void mixedSegmentsAccumulateFaresCorrectly() {
        // 2km normal (2×2.10=4.20) + 2km overnight (2×3.90=7.80) = 12.00
        var ride = new Ride();
        ride.addSegment(2, LocalDateTime.of(2024, 3, 4, 10, 0));
        ride.addSegment(2, LocalDateTime.of(2024, 3, 4, 23, 0));
        ride.calculateFare();
        assertEquals(12.00, ride.getFare(), 0.001);
    }

    // --- Validation ---

    @Test
    void negativeDistanceThrowsException() {
        assertThrows(IllegalArgumentException.class,
            () -> new Segment(-1, LocalDateTime.of(2024, 3, 4, 10, 0)));
    }

    @Test
    void zeroDistanceThrowsException() {
        assertThrows(IllegalArgumentException.class,
            () -> new Segment(0, LocalDateTime.of(2024, 3, 4, 10, 0)));
    }
}
