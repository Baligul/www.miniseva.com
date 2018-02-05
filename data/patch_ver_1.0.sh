#!/bin/bash

# Miniseva Database Installation Script
# ========================================
# PostgreSQL 9.4 on Linux/Mac

# Setup Instructions
# ------------------
#
# Open a terminal, then run the following commands:
#
# vi ~/.pgpass
#
# Add the following 2 lines to .pgpass (Without the first # on each line)
#    #hostname:port:database:username:password
#    localhost:5432:miniseva:rs:pass.word
#
# Save and close .pgpass
#
# chmod 600 ~/.pgpass
#
# Close and re-open the terminal
#
# cd ~/repos/www.miniseva.com/data
#
# ./patch_ver_1.0.sh
#


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

# Run the patch to update the existing latest DB to support spring project
# ------------------------------------------------------------------------------
echo "=== Updating the latest RS db using patch ==="
#$SUDO -u $ADMIN_ROLE $PSQL -d $database < ./pg/update/patch_ver_1.0.sql
$PSQL -h localhost -p 5432 -U rs -w -d $database  < ./pg/update/patch_ver_1.0.sql > restore-db.log 2>&1 && grep -i error restore-db.log
