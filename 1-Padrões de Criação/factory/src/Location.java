import java.time.LocalDateTime;

public class Location {
    Coord coord;
    final LocalDateTime date;

    public Location(double lat, double lon, LocalDateTime date) {
        this.coord = new Coord(lat, lon);
        this.date = date;
    }
}
