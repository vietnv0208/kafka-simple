# Kafka service
### set up kafka
1. install & start docker
2. init and start kafka:
   `docker-compose up -d`

### start/stop Kafka service
 Start: `docker-compose start`

 Stop: `docker-compose stop`
 
## Quick start
Guild: https://kafka.apache.org/quickstart

### Example:
CREATE A TOPIC TO STORE YOUR EVENTS<br>
`bin/kafka-topics.sh --create --topic cdata --bootstrap-server localhost:29092`

describe topic: 
`bin/kafka-topics.sh --describe --topic cdata --bootstrap-server localhost:29092`

WRITE SOME EVENTS INTO THE TOPIC<br>
`bin/kafka-console-producer.sh --topic cdata --bootstrap-server localhost:29092`

READ THE EVENTS<br>
`bin/kafka-console-consumer.sh --topic cdata --from-beginning --bootstrap-server localhost:29092`
