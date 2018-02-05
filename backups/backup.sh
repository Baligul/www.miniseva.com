#!/bin/bash

VERSION=$1
DATE=`date +%Y.%m.%d`

if [ -z $VERSION ]; then
    VERSION=01
fi

pg_dump --create -h localhost -U rs -p 5432 miniseva | gzip > miniseva-${DATE}-${VERSION}.sql.gz
