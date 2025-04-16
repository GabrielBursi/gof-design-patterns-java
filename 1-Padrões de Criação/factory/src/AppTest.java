
import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Test;

public class AppTest {

    public void testUpdateDistanceRideLocation() throws Exception {

        RideRepository rideRepository = new RideRepositoryMemory();
        SegmentRepository segmentRepository = new SegmentRepositoryMemory();

        Ride ride = DistanceRide.create(-27.584905257808835, -48.545022195325124,
                LocalDateTime.parse("2021-03-01T10:00:00"));
        rideRepository.save(ride);

        UpdateLocation updateLocation = new UpdateLocation(rideRepository, segmentRepository);
        UpdateLocationInputDTO input = new UpdateLocationInputDTO(
                ride.getRideId(),
                -27.496887588317275,
                -48.522234807851476,
                LocalDateTime.parse("2021-03-01T12:00:00"));
        updateLocation.execute(input);

        CalculateFare calculateFare = new CalculateFare(rideRepository, segmentRepository);
        CaculateFareOutputDTO output = calculateFare.execute(ride.getRideId());

        assertEquals(40.0, output.fare(), 0.0001);
    }

    @Test
    public void testUpdateTimeRideLocation() throws Exception {

        RideRepository rideRepository = new RideRepositoryMemory();
        SegmentRepository segmentRepository = new SegmentRepositoryMemory();

        Ride ride = TimeRide.create(-27.584905257808835, -48.545022195325124,
                LocalDateTime.parse("2021-03-01T10:00:00"));
        rideRepository.save(ride);

        UpdateLocation updateLocation = new UpdateLocation(rideRepository, segmentRepository);
        UpdateLocationInputDTO input = new UpdateLocationInputDTO(
                ride.getRideId(),
                -27.496887588317275,
                -48.522234807851476,
                LocalDateTime.parse("2021-03-01T12:00:00"));
        updateLocation.execute(input);

        CalculateFare calculateFare = new CalculateFare(rideRepository, segmentRepository);
        CaculateFareOutputDTO output = calculateFare.execute(ride.getRideId());

        assertEquals(120.0, output.fare(), 0.0001);
    }
}
