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

# Create tables
# ------------------------------------------------------------------------------
echo "=== Creating tables ==="
echo ":: Version"
echo "- version"
$PSQL -h localhost -p 5432 -U rs -w -d $database  < ./pg/2.0/2.0.1/tables/version.sql

echo ":: Authentication"

echo "- account"
$PSQL -h localhost -p 5432 -U rs -w -d $database  < ./pg/2.0/2.0.1/tables/account.sql
echo "- role"
$PSQL -h localhost -p 5432 -U rs -w -d $database  < ./pg/2.0/2.0.1/tables/role.sql
echo "- account_role_map"
$PSQL -h localhost -p 5432 -U rs -w -d $database  < ./pg/2.0/2.0.1/tables/account_role_map.sql
echo "- persistent_logins"
$PSQL -h localhost -p 5432 -U rs -w -d $database  < ./pg/2.0/2.0.1/tables/persistent_logins.sql
echo "- spring_session"
$PSQL -h localhost -p 5432 -U rs -w -d $database  < ./pg/2.0/2.0.1/tables/spring_session.sql
echo "- UserConnection"
$PSQL -h localhost -p 5432 -U rs -w -d $database  < ./pg/2.0/2.0.1/tables/UserConnection.sql
echo "- categories"
$PSQL -h localhost -p 5432 -U rs -w -d $database  < ./pg/2.0/2.0.1/tables/categories.sql
echo "- sub_categories"
$PSQL -h localhost -p 5432 -U rs -w -d $database  < ./pg/2.0/2.0.1/tables/sub_categories.sql
echo "- web_contact_us"
$PSQL -h localhost -p 5432 -U rs -w -d $database  < ./pg/2.0/2.0.1/tables/web_contact_us.sql
echo "- orders"
$PSQL -h localhost -p 5432 -U rs -w -d $database  < ./pg/2.0/2.0.1/tables/orders.sql
echo "- coupons"
$PSQL -h localhost -p 5432 -U rs -w -d $database  < ./pg/2.0/2.0.1/tables/coupons.sql
echo "- account_subcategory_map"
$PSQL -h localhost -p 5432 -U rs -w -d $database  < ./pg/2.0/2.0.1/tables/account_subcategory_map.sql
echo "- order_subcategory_map"
$PSQL -h localhost -p 5432 -U rs -w -d $database  < ./pg/2.0/2.0.1/tables/order_subcategory_map.sql

# Alter tables
# ------------------------------------------------------------------------------
#echo "=== Altering tables ==="
#echo ":: CRM"
#echo "- crm_company"
#$SUDO -u $username $PSQL -d $database < ./pg/0.0/0.0.3/tables/crm_company.sql

# Default data
# ------------------------------------------------------------------------------

echo "=== Inserting default data ==="
$PSQL -h localhost -p 5432 -U rs -w -d $database  < ./pg/2.0/2.0.1/data/role.sql
$PSQL -h localhost -p 5432 -U rs -w -d $database  < ./pg/2.0/2.0.1/data/account.sql
$PSQL -h localhost -p 5432 -U rs -w -d $database  < ./pg/2.0/2.0.1/data/account_role_map.sql
$PSQL -h localhost -p 5432 -U rs -w -d $database  < ./pg/2.0/2.0.1/data/categories.sql
$PSQL -h localhost -p 5432 -U rs -w -d $database  < ./pg/2.0/2.0.1/data/sub_categories.sql
$PSQL -h localhost -p 5432 -U rs -w -d $database  < ./pg/2.0/2.0.1/data/insert_courses.sql
$PSQL -h localhost -p 5432 -U rs -w -d $database  < ./pg/2.0/2.0.1/data/update_courses.sql

echo "=== Publish all courses, exams and documentation ==="
$PSQL -h localhost -p 5432 -U rs -w -d $database  < ./pg/2.0/2.0.1/data/publish_all_courses.sql
$PSQL -h localhost -p 5432 -U rs -w -d $database  < ./pg/2.0/2.0.1/data/publish_all_exams.sql
$PSQL -h localhost -p 5432 -U rs -w -d $database  < ./pg/2.0/2.0.1/data/publish_all_documentations.sql

# Create triggers
# ------------------------------------------------------------------------------

# echo "=== Creating triggers ==="
# echo "- article"
# $PSQL -h localhost -p 5432 -U rs -w -d $database  < ./pg/0.0/0.0.3/tables/article_trigger.sql

# Update permissions
# ------------------------------------------------------------------------------

echo "=== Updating permissions ==="
$SUDO -u $ADMIN_ROLE $PSQL -d $database < ./pg/2.0/2.0.1/permissions/grant.sql

# Update version
# ------------------------------------------------------------------------------

echo "=== Updating version ==="
$PSQL -h localhost -p 5432 -U rs -w -d $database  < ./pg/2.0/2.0.1/data/version.sql
