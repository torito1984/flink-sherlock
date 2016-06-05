# Samples with Apache Flink and Kafka

This code is meant to be run in connection with a Kafka producer (see https://github.com/torito1984/kafka-doyle-generator.git).

This project includes a series of examples that show how to connect Apache Flink with Kafka in order to build pipelines. 
It includes several examples. The first ones are batch examples to point out the similarities between using both.
Once the project is build, this can be a sample session to see different behaviours

// Samples Flink
// Start a Flink session in Yarn. In order to have a system available run for example a Hortonworks HDP installation locally.
./start-flink-session.sh 

// Put a file in HDFS to be able to run batch examples
hadoop fs -put files/doyle.txt .
// Check that the file is there
hadoop fs -ls

// Run the word count example
./run.sh --input hdfs:///user/osboxes/doyle.txt --output hdfs:///user/osboxes/sherlock-wordcount.txt
// Get the result file from HDFS
hadoop fs -getmerge sherlock-wordcount.txt sherlock-wordcount.txt
// Check that the batch counting has been done
cat sherlock-wordcount.txt 

// Extract all occurrence of locations extracted with Stanford NLP. Use the stream on a solely file.
./run-locations.sh --input hdfs:///user/osboxes/doyle.txt --output  hdfs:///user/osboxes/locations
// Get the result and check extraction and counting was done ok
hadoop fs -getmerge locations sherlock-locations.txt
cat sherlock-locations.txt
// Check the count of "United States"
cat sherlock-locations.txt | grep "United States"

// Extract all occurrence of locations extracted with Stanford NLP. Use the batch on the same file. Check differences
./run-locations-batch.sh --input hdfs:///user/osboxes/doyle.txt --output  hdfs:///user/osboxes/locations-batch
hadoop fs -getmerge locations-batch sherlock-locations-batch.txt
cat sherlock-locations-batch.txt
cat sherlock-locations-batch.txt | grep "United States"

// Run now a windowed version of the extract-count
./run-locations-window.sh --input hdfs:///user/osboxes/doyle.txt --output  hdfs:///user/osboxes/locations-window
hadoop fs -getmerge locations-window sherlock-locations-window.txt
cat sherlock-locations-window.txt | grep "United States"

// Now lets combine Flink + Kafka

// Remember first run the producer!

// Publish a topic with the locations
./run-locations-kafka-publish.sh --topic doyle-episodes --output doyle-locations --bootstrap.servers localhost:6667 --zookeeper.connect localhost:2181 --group.id flink-locations

// Check that the processing is happening correctly
/usr/hdp/current/kafka-broker/bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic doyle-locations

The code has been tested with Kafka 0.9.0.4 included in Hortonworks HDP 2.4.0. It supposes that Kafka is available in localhost:6667 and Zookeeper in localhost:2181. These locations can be configured in the scripts.
