package org.branas.io;

public interface FareCalculator {

    FareCalculator setNext(FareCalculator next);

    double calculate(Segment segment);
}
