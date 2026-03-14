CREATE UNIQUE INDEX IF NOT EXISTS ux_users_email_normalized_active
    ON users (email_normalized)
    WHERE deleted_at IS NULL;