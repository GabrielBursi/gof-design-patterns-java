import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Test;

public class RideTest {

    @Test
    public void shouldCreateAndCalculateFareForDistanceRide() {
        DistanceRide ride = DistanceRide.create(-27.584905257808835, -48.545022195325124, LocalDateTime.parse("2021-03-01T10:00:00"));

        Location lastLocation = new Location(-27.584905257808835, -48.545022195325124, LocalDateTime.parse("2021-03-01T10:00:00"));
        Location newLocation = new Location(-27.496887588317275, -48.522234807851476, LocalDateTime.parse("2021-03-01T12:00:00"));

        Segment segment = new DistanceSegment(ride.getRideId(), lastLocation, newLocation);

        ride.updateLocation(newLocation);

        double fare = ride.calculateFare(List.of(segment));

        assertEquals(40.0, fare, 0.0001);
    }

    @Test
    public void shouldCreateAndCalculateFareForTimeRide() {
        TimeRide ride = TimeRide.create(-27.584905257808835, -48.545022195325124, LocalDateTime.parse("2021-03-01T10:00:00"));

        Location lastLocation = new Location(-27.584905257808835, -48.545022195325124, LocalDateTime.parse("2021-03-01T10:00:00"));
        Location newLocation = new Location(-27.496887588317275, -48.522234807851476, LocalDateTime.parse("2021-03-01T12:00:00"));

        Segment segment = new TimeSegment(ride.getRideId(), lastLocation, newLocation);

        ride.updateLocation(newLocation);

        double fare = ride.calculateFare(List.of(segment));

        assertEquals(120.0, fare, 0.0001);
    }
}
