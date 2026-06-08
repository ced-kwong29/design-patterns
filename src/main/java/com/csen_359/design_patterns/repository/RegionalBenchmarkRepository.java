package com.csen_359.design_patterns.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csen_359.design_patterns.domain.RegionalBenchmark;
import com.csen_359.design_patterns.domain.Season;
import com.csen_359.design_patterns.domain.UsageCategory;


public interface RegionalBenchmarkRepository extends JpaRepository<RegionalBenchmark, Long> {

    Optional<RegionalBenchmark> findByRegionCodeAndCategoryAndSeason(
            String regionCode, UsageCategory category, Season season);
}
