DO
$body$
BEGIN
    IF NOT EXISTS(
            SELECT *
            FROM pg_catalog.pg_user
            WHERE usename = 'ms')
    THEN
        CREATE ROLE ms WITH PASSWORD '2d7D2f3B*MiniSeva';
    END IF;
END
$body$;

ALTER ROLE ms NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE NOREPLICATION LOGIN;
