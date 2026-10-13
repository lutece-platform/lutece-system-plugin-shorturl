-- liquibase formatted sql

--
-- Pre-execution script (plugin-liquibase >= 2.0.2), run before the version resolution of the main update.
-- Records the version 1.0.3 of a Lutece 7 site that holds the shorturl table and no version for shorturl,
-- so that the main run plays the upgrade scripts instead of the creation script.
-- False on a fresh install and on a site already followed by Liquibase (MARK_RAN).
--

-- changeset shorturl:prerun-version-of-a-v7-site-1.0.3 dbms:mariadb,mysql
-- preconditions onFail:MARK_RAN onError:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM core_datastore WHERE entity_key LIKE '%core.plugins.status.shorturl.version'
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM shorturl WHERE 1 = 0
INSERT INTO core_datastore (entity_key, entity_value)
SELECT 'core.plugins.status.shorturl.version', '1.0.3' FROM information_schema.tables
WHERE table_schema = database() AND table_name = 'shorturl'
AND NOT EXISTS (SELECT 1 FROM core_datastore WHERE entity_key LIKE '%core.plugins.status.shorturl.version');

-- changeset shorturl:prerun-version-of-a-v7-site-1.0.3-postgresql dbms:postgresql
-- preconditions onFail:MARK_RAN onError:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM core_datastore WHERE entity_key LIKE '%core.plugins.status.shorturl.version'
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM shorturl WHERE 1 = 0
INSERT INTO core_datastore (entity_key, entity_value)
SELECT 'core.plugins.status.shorturl.version', '1.0.3' FROM information_schema.tables
WHERE table_schema = current_schema() AND table_name = 'shorturl'
AND NOT EXISTS (SELECT 1 FROM core_datastore WHERE entity_key LIKE '%core.plugins.status.shorturl.version');
