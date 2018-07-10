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
#    server:5432:miniseva:ms:2d7D2f3B*MiniSeva
#
# Save and close .pgpass
#
# chmod 600 ~/.pgpass
#
# Close and re-open the terminal
#
# cd ~/repos/www.miniseva.com/data
#
# ./create-pg-database.sh
#


# Variables
# ------------------------------------------------------------------------------
server=ec2-54-83-33-213.compute-1.amazonaws.com
database=d462psee94r4il
port=5432
dbuser=sobkkpvkzyinpg

OS=`uname -s`
SUDO=`which sudo`
PSQL=`which psql`
ADMIN_DB="postgres"
ADMIN_ROLE="postgres"

if [ "$OS" == "Darwin" ] ; then
    ADMIN_DB="template1"
    ADMIN_ROLE=`whoami`
fi

# Create tables
# ------------------------------------------------------------------------------
echo "=== Creating tables ==="
echo "- account"
$PSQL -h $server -p 5432 -U $dbuser -w -d $database  < ./pg/tables/account.sql
echo "- role"
$PSQL -h $server -p 5432 -U $dbuser -w -d $database  < ./pg/tables/role.sql
echo "- account_role_map"
$PSQL -h $server -p 5432 -U $dbuser -w -d $database  < ./pg/tables/account_role_map.sql
echo "- persistent_logins"
$PSQL -h $server -p 5432 -U $dbuser -w -d $database  < ./pg/tables/persistent_logins.sql
echo "- spring_session"
$PSQL -h $server -p 5432 -U $dbuser -w -d $database  < ./pg/tables/spring_session.sql
echo "- UserConnection"
$PSQL -h $server -p 5432 -U $dbuser -w -d $database  < ./pg/tables/UserConnection.sql
echo "- web_contact_us"
$PSQL -h $server -p 5432 -U $dbuser -w -d $database  < ./pg/tables/web_contact_us.sql
echo "- card"
$PSQL -h $server -p 5432 -U $dbuser -w -d $database  < ./pg/tables/card.sql
echo "- lead"
$PSQL -h $server -p 5432 -U $dbuser -w -d $database  < ./pg/tables/lead.sql
echo "- block"
$PSQL -h $server -p 5432 -U $dbuser -w -d $database  < ./pg/tables/block.sql
echo "- customer"
$PSQL -h $server -p 5432 -U $dbuser -w -d $database  < ./pg/tables/customer.sql
echo "- version"
$PSQL -h $server -p 5432 -U $dbuser -w -d $database  < ./pg/tables/version.sql
echo "- product"
$PSQL -h $server -p 5432 -U $dbuser -w -d $database  < ./pg/tables/product.sql
echo "- item"
$PSQL -h $server -p 5432 -U $dbuser -w -d $database  < ./pg/tables/item.sql
echo "- slot"
$PSQL -h $server -p 5432 -U $dbuser -w -d $database  < ./pg/tables/slot.sql
echo "- schedule"
$PSQL -h $server -p 5432 -U $dbuser -w -d $database  < ./pg/tables/schedule.sql
echo "- orders"
$PSQL -h $server -p 5432 -U $dbuser -w -d $database  < ./pg/tables/orders.sql

# Grant role permissions
# ------------------------------------------------------------------------------
echo "=== Granting role permissions ==="
$SUDO -u $ADMIN_ROLE $PSQL -d $database < ./pg/grant-role-permissions.sql

# Default data
# ------------------------------------------------------------------------------

echo "=== Inserting default data ==="
echo "- account"
$PSQL -h $server -p 5432 -U $dbuser -w -d $database  < ./pg/data/account.sql
echo "- role"
$PSQL -h $server -p 5432 -U $dbuser -w -d $database  < ./pg/data/role.sql
echo "- account_role_map"
$PSQL -h $server -p 5432 -U $dbuser -w -d $database  < ./pg/data/account_role_map.sql

# Update version
# ------------------------------------------------------------------------------

echo "=== Updating version ==="
$PSQL -h $server -p 5432 -U $dbuser -w -d $database  < ./pg/data/version.sql
