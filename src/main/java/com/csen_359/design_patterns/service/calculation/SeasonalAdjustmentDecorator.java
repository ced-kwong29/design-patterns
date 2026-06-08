package com.csen_359.design_patterns.service.calculation;

import java.util.List;

import com.csen_359.design_patterns.domain.Season;
import com.csen_359.design_patterns.domain.UsageEntry;

/**
 * Decorator - adjusts a wrapped calculation for the season
 */
public class SeasonalAdjustmentDecorator implements UsageCalculator {

    private final UsageCalculator delegate;
    private final Season season;

    public SeasonalAdjustmentDecorator(UsageCalculator delegate, Season season) {
        this.delegate = delegate;
        this.season = season;
    }

    @Override
    public double calculate(List<UsageEntry> entries) {
        double base = delegate.calculate(entries);
        return base * seasonFactor(season);
    }

    private static double seasonFactor(Season season) {
        return switch (season) {
            case SUMMER ->
                // usage runs ~10% high in summer
                0.90;      
            case WINTER ->
                // ~10% low in winter
                1.10;        
            case SPRING, AUTUMN ->
                // treated as the neutral baseline
                1.0; 
        };
    }
}
