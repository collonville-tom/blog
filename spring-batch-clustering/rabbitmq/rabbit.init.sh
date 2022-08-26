#!/bin/bash

source $(dirname $0)/rabbit-utils.sh

echo "Waiting that rabbitmq service is started"
waiting_amq_started

create_queue "partitionTask"
create_queue "partitionResults"

create_queue "chunkTask"
create_queue "chunkResults"


