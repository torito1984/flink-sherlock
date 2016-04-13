# Prepare
sudo ./kafka-manager-1.3.0.4/bin/kafka-manager

## Sample Kafka
./runFileCreator.sh mail 3
./runProducer.sh mail mail
# Do this 4 times? What happens?
./runConsumer.sh mail earliest reader1

cd kafka-stream-samples/
./runContentFeed.sh doyle.txt doyle

## Samples Flink
./start-flink-session.sh 
cd repos/flink-sherlock
hadoop fs -put files/doyle.txt .
hadoop fs -ls

./run.sh --input hdfs:///user/osboxes/doyle.txt --output hdfs:///user/osboxes/sherlock-wordcount.txt
hadoop fs -getmerge sherlock-wordcount.txt sherlock-wordcount.txt
cat sherlock-wordcount.txt 

# This is longer, have a look at the queue
./run-locations.sh --input hdfs:///user/osboxes/doyle.txt --output  hdfs:///user/osboxes/locations
hadoop fs -getmerge locations sherlock-locations.txt
cat sherlock-locations.txt
cat sherlock-locations.txt | grep "United States"

./run-locations-batch.sh --input hdfs:///user/osboxes/doyle.txt --output  hdfs:///user/osboxes/locations-batch
#Look also at the scheduling!
hadoop fs -getmerge locations-batch sherlock-locations-batch.txt
cat sherlock-locations-batch.txt
cat sherlock-locations-batch.txt | grep "United States"

./run-locations-window.sh --input hdfs:///user/osboxes/doyle.txt --output  hdfs:///user/osboxes/locations-window
hadoop fs -getmerge locations-window sherlock-locations-window.txt
cat sherlock-locations-window.txt | grep "United States"

## Flink + Kafka

# Produce
cd repos/kafka-doyle-generator/
./runDoyleFeed.sh doyle.txt doyle-episodes
cd repos/flink-sherlock/

# Publish a topic
./run-locations-kafka-publish.sh --topic doyle-episodes --output doyle-locations --bootstrap.servers localhost:6667 --zookeeper.connect localhost:2181 --group.id flink-locations

/usr/hdp/current/kafka-broker/bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic doyle-locations

# Feed a use case (Hbase)




