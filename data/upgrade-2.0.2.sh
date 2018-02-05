#!/bin/bash

# To the maximum extent possible, updates should be idempotent.

# MiniSeva Database Upgrade Script
# ====================================
# PostgreSQL 9.x on Linux/Mac

# Variables
# ------------------------------------------------------------------------------
server=localhost
database=miniseva
port=5432
username=rs

OS=`uname -s`
SUDO=`which sudo`
PSQL=`which psql`
ADMIN_DB="postgres"
ADMIN_ROLE="postgres"

if [ "$OS" == "Darwin" ] ; then
    ADMIN_DB="template1"
    ADMIN_ROLE=`whoami`
fi

# Default data
# ------------------------------------------------------------------------------

echo "=== Inserting default data ==="
#$SUDO -u $username $PSQL -d $database < ./pg/2.0/2.0.2/data/update_sub_categories.sql
$PSQL -h localhost -p 5432 -U rs -w -d $database  < ./pg/2.0/2.0.2/data/update_sub_categories.sql > restore-db.log 2>&1 && grep -i error restore-db.log

# Update version
# ------------------------------------------------------------------------------

echo "=== Updating version ==="
#$PSQL -u $username $PSQL -d $database < ./pg/2.0/2.0.2/data/version.sql
$PSQL -h localhost -p 5432 -U rs -w -d $database  < ./pg/2.0/2.0.2/data/version.sql > restore-db.log 2>&1 && grep -i error restore-db.log
