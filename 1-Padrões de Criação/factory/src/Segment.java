public abstract class Segment {
    final String rideId;
    final Location from;
    final Location to;

    public Segment(String rideId, Location from, Location to) {
        this.rideId = rideId;
        this.from = from;
        this.to = to;
    }
}
