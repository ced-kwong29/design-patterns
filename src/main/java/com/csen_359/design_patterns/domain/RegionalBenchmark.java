package com.csen_359.design_patterns.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * reference data: average daily water usage per region,
 * category and season. Consumed by the regional benchmark decorator.
 */
@Entity
@Table(name = "regional_benchmarks")
public class RegionalBenchmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "region_code", nullable = false, length = 16)
    private String regionCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private UsageCategory category;

    @Column(name = "avg_litres_per_day", nullable = false)
    private double avgLitresPerDay;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private Season season;

    protected RegionalBenchmark() {
    }

    public Long getId() {
        return id;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public UsageCategory getCategory() {
        return category;
    }

    public double getAvgLitresPerDay() {
        return avgLitresPerDay;
    }

    public Season getSeason() {
        return season;
    }
}
