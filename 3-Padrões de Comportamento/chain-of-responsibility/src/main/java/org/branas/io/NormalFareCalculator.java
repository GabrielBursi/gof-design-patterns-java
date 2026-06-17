package org.branas.io;

public class NormalFareCalculator extends BaseFareCalculator {

    private static final double RATE = 2.10;

    @Override
    public double calculate(Segment segment) {
        if (!segment.isOvernight() && !segment.isSunday()) {
            return segment.getDistance() * RATE;
        }
        return passToNext(segment);
    }
}
