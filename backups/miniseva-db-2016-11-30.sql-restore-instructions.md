sudo -u postgres psql -c "DROP DATABASE miniseva"
sudo -u postgres psql -c "CREATE DATABASE miniseva WITH OWNER ms";

psql -h localhost -p 5432 -U ms -w -d miniseva < miniseva-db-2016-11-30.sql > restore-db.log 2>&1 && grep -i error restore-db.log
