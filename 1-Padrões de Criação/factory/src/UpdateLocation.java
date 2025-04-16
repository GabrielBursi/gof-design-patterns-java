public class UpdateLocation {
    private final RideRepository rideRepository;
    private final SegmentRepository segmentRepository;

    public UpdateLocation(RideRepository rideRepository, SegmentRepository segmentRepository) {
        this.rideRepository = rideRepository;
        this.segmentRepository = segmentRepository;
    }

    void execute (UpdateLocationInputDTO input) {
        Ride ride = this.rideRepository.getById(input.rideId());
		Location newLocation = new Location(input.lat(), input.lon(), input.date());
		Segment segment = ride.createSegment(ride.getLastLocation(), newLocation);
		ride.updateLocation(newLocation);
		this.rideRepository.update(ride);
		this.segmentRepository.save(segment);
    }
}
