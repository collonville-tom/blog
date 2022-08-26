#!/bin/bash

USER=admin
PWD=admin
RMQ_HOST=rabbitmq

waiting_amq_started()
{
  local state="nok"
  while [ "$state" != "ok" ]
  do
    #state=$(curl -u $USER:$PWD -G "http://$RMQ_HOST:15672/api/healthchecks/node")
    state=$(wget http://$USER:$PWD@$RMQ_HOST:15672/api/healthchecks/node; [ -f node ] && cat node)
    [ -f node ] && rm node
    echo "Waiting $RMQ_HOST healthcheck is ok and it is $state"
    state=$(expr $state : ".*:.\(..\).*")
    echo "$state"
    sleep 1s
  done
  sleep 20s
}

create_flux()
{
    local exchange_name=$1
    local queue_name=$2
    local exchange_type=$3 
    local args=""
    if [ "$4" != "" ]
    then 
      args="routing_key=$4"
    fi
    echo "Declare exchange $exchange_name with type=$exchange_type and queue $queue_name with $args"
    rabbitmqadmin declare exchange --host=$RMQ_HOST -u $USER -p $PWD name=$exchange_name durable=true type=$exchange_type
    rabbitmqadmin declare queue --host=$RMQ_HOST -u $USER -p $PWD name=$queue_name durable=true 
    rabbitmqadmin declare binding --host=$RMQ_HOST -u $USER -p $PWD source=$exchange_name destination=$queue_name $args
}

create_queue()
{
    local queue_name=$1
    echo "Declare queue $queue_name"
    rabbitmqadmin declare queue --host=$RMQ_HOST -u $USER -p $PWD name=$queue_name durable=true 
}