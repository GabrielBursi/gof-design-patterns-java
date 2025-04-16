public class DistanceSegment extends Segment {
    public DistanceSegment(String rideId, Location from, Location to) {
        super(rideId, from, to);
    }

    public int getDistance() {
        final int earthRadius = 6371;
        final double degreesToRadians = Math.PI / 180;

        double deltaLat = (to.coord.getLatitude() - from.coord.getLatitude()) * degreesToRadians;
        double deltaLon = (to.coord.getLongitude() - from.coord.getLongitude()) * degreesToRadians;

        double fromLatRad = from.coord.getLatitude() * degreesToRadians;
        double toLatRad = to.coord.getLatitude() * degreesToRadians;

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(fromLatRad) * Math.cos(toLatRad) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return (int) Math.round(earthRadius * c);
    }
}
