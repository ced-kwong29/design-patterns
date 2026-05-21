-- Seeded, read-only reference data for the regional benchmark decorator.
-- Litres-per-day figures are illustrative placeholders for the DEFAULT region.

INSERT INTO regional_benchmarks (region_code, category, avg_litres_per_day, season) VALUES
    ('DEFAULT', 'SHOWER',     65.0, 'SUMMER'),
    ('DEFAULT', 'SHOWER',     70.0, 'WINTER'),
    ('DEFAULT', 'BATH',       80.0, 'SUMMER'),
    ('DEFAULT', 'BATH',       90.0, 'WINTER'),
    ('DEFAULT', 'LAUNDRY',    50.0, 'SUMMER'),
    ('DEFAULT', 'LAUNDRY',    50.0, 'WINTER'),
    ('DEFAULT', 'DISHWASHER', 15.0, 'SUMMER'),
    ('DEFAULT', 'DISHWASHER', 15.0, 'WINTER'),
    ('DEFAULT', 'GARDEN',     90.0, 'SUMMER'),
    ('DEFAULT', 'GARDEN',     10.0, 'WINTER'),
    ('DEFAULT', 'DRINKING',    3.0, 'SUMMER'),
    ('DEFAULT', 'DRINKING',    3.0, 'WINTER'),
    ('DEFAULT', 'OTHER',      20.0, 'SUMMER'),
    ('DEFAULT', 'OTHER',      20.0, 'WINTER');
