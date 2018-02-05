-- Update permissions is required on the mac
-- However, it may be a good idea to ensure all permissions are correctly set
-- on each upgrade.
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO ms;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public to ms;
GRANT USAGE ON SCHEMA public TO ms;