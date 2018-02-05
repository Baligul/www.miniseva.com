psql -h localhost -p 5432 -U rs -w -d miniseva < patch_ver_1.0.sql > patch-db.log 2>&1 && grep -i error patch-db.log
