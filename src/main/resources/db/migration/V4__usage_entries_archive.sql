-- Phase 9 - cold-storage archive for usage entries older than two years.
-- DataCleanupJob copies rows here (INSERT ... SELECT) before deleting them
-- from usage_entries, preserving history without bloating the hot table.

CREATE TABLE usage_entries_archive (
    id               BIGINT           NOT NULL PRIMARY KEY,
    user_id          BIGINT           NOT NULL,
    category         VARCHAR(32)      NOT NULL,
    litres           DOUBLE PRECISION NOT NULL,
    duration_minutes INTEGER,
    logged_at        TIMESTAMP        NOT NULL,
    notes            VARCHAR(500),
    adjusted_litres  DOUBLE PRECISION,
    created_at       TIMESTAMP        NOT NULL,
    archived_at      TIMESTAMP        NOT NULL
);

CREATE INDEX idx_archive_user_logged
    ON usage_entries_archive (user_id, logged_at);
