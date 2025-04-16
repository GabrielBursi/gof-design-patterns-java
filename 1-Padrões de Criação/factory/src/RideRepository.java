public interface RideRepository {
    void save (Ride ride);
	void update (Ride ride);
	Ride getById (String rideId);
}
