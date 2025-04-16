import java.util.ArrayList;
import java.util.List;

public class RideRepositoryMemory implements RideRepository {
    private final List<Ride> rides;

    public RideRepositoryMemory() {
        this.rides = new ArrayList<>();
    }

    @Override
    public void save(Ride ride) {
        rides.add(ride);
    }

    @Override
    public void update(Ride ride) {
        for (int i = 0; i < rides.size(); i++) {
            if (rides.get(i).getRideId().equals(ride.getRideId())) {
                rides.set(i, ride);
                return;
            }
        }
        throw new RuntimeException("Ride not found");
    }

    @Override
    public Ride getById(String rideId) {
        return rides.stream()
                .filter(ride -> ride.getRideId().equals(rideId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Ride not found"));
    }
}