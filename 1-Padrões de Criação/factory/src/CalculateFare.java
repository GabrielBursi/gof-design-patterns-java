import java.util.List;

public class CalculateFare {
    private final RideRepository rideRepository;
    private final SegmentRepository segmentRepository;

    public CalculateFare(RideRepository rideRepository, SegmentRepository segmentRepository) {
        this.rideRepository = rideRepository;
        this.segmentRepository = segmentRepository;
    }

    public CaculateFareOutputDTO execute (String rideId) {
        Ride ride = this.rideRepository.getById(rideId);
        List<Segment> segments = this.segmentRepository.listByRideId(rideId);
        double fare = ride.calculateFare(segments);
        return new CaculateFareOutputDTO(fare);
    }

}
