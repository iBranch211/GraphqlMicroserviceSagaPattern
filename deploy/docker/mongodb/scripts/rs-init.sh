#!/bin/bash

DELAY=25

HOSTNAME=`hostname`

OPTS=`getopt -o h: --long hostname: -n 'parse-options' -- "$@"`
if [ $? != 0 ] ; then echo "Failed parsing options." >&2 ; exit 1 ; fi

echo "$OPTS"
eval set -- "$OPTS"

while true; do
    case "$1" in
-h | --hostname )     HOSTNAME=$2;        shift; shift ;;
-- ) shift; break ;;
* ) break ;;
esac
done
echo "Using HOSTNAME='$HOSTNAME'"

mongo -u "$MONGO_INITDB_ROOT_USERNAME" -p "$MONGO_INITDB_ROOT_PASSWORD" localhost:27017/admin <<EOF
var config = {
    "_id": "rs0",
    "version": 1,
    "members": [
        {
            "_id": 1,
            "host": "${HOSTNAME}:27017"
        }
    ]
};
rs.initiate(config, { force: true });
EOF

echo "****** Waiting for ${DELAY} seconds for replicaset configuration to be applied ******"

sleep $DELAY

mongo -u "$MONGO_INITDB_ROOT_USERNAME" -p "$MONGO_INITDB_ROOT_PASSWORD" localhost:27017/admin < /scripts/init.js