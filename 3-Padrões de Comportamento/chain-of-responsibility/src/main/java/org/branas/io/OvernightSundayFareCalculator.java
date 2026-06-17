package org.branas.io;

public class OvernightSundayFareCalculator extends BaseFareCalculator {

    private static final double RATE = 5.00;

    @Override
    public double calculate(Segment segment) {
        if (segment.isOvernight() && segment.isSunday()) {
            return segment.getDistance() * RATE;
        }
        return passToNext(segment);
    }
}
