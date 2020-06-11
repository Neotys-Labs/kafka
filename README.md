# Kafka

## kafka-producer
This advanced action allows you to publish messages to Kafka topics from [NeoLoad](https://www.neotys.com/neoload/overview)
More information on Kafka Producers can be found [here](http://kafka.apache.org/0102/documentation/#producerconfigs)

 | Property | Value |
 | -----| -------------- |
 | Maturity | Experimental |
 | Author   | Neotys Professional Services |
 | License  | [BSD Simplified](https://www.neotys.com/documents/legal/bsd-neotys.txt) |
 | NeoLoad  | 6.1 (Enterprise or Professional Edition w/ Integration & Advanced Usage and NeoLoad Web option required)|
 | Requirements | |
 | Bundled in NeoLoad | No
 | Download Binaries | See the [latest release](https://github.com/Neotys-Labs/kafka/releases/latest)
 
 ## Installation
 
 1. Download the [latest release](https://github.com/Neotys-Labs/kafka/releases/latest)
 1. Read the NeoLoad documentation to see [How to install a custom Advanced Action](https://www.neotys.com/documents/doc/neoload/latest/en/html/#25928.htm)
 
 ## Usage
 
 1. Read the NeoLoad documentation to see [How to use an Advanced action](https://www.neotys.com/documents/doc/neoload/latest/en/html/#25929.htm)
 
 ## Parameters
 | Name             | Description |
 | -----            | ----- |
 | topic | The topic to send the message to. |
 | message | The message to send |
 | destination | The name or IP of the destination broker |
 | port (optional) | The port of the destination server. Default value is "9092". |
 | key.serializer (optional) | Serializer class for key that implements the Serializer interface. Default value is "org.apache.kafka.common.serialization.StringSerializer". |
 | value.serializer (optional) | Serializer class for value that implements the Serializer interface. Default value is "org.apache.kafka.common.serialization.StringSerializer". |
 | acks (optional) | The number of acknowledgments the producer requires the leader to have received before considering a request complete. This controls the durability of records that are sent. The following settings are allowed: <br> <ul><li> acks=0 If set to zero then the producer will not wait for any acknowledgment from the server at all. The record will be immediately added to the socket buffer and considered sent. No guarantee can be made that the server has received the record in this case, and the retries configuration will not take effect (as the client won't generally know of any failures). The offset given back for each record will always be set to -1.</li><li>acks=1 This will mean the leader will write the record to its local log but will respond without awaiting full acknowledgement from all followers. In this case should the leader fail immediately after acknowledging the record but before the followers have replicated it then the record will be lost.</li><li>acks=all This means the leader will wait for the full set of in-sync replicas to acknowledge the record. This guarantees that the record will not be lost as long as at least one in-sync replica remains alive. This is the strongest available guarantee. This is equivalent to the acks=-1 setting. Default value is "1".</li></ul> |
 | retries (optional) | Setting a value greater than zero will cause the client to resend any record whose send fails with a potentially transient error. Note that this retry is no different than if the client resent the record upon receiving the error. Allowing retries without setting max.in.flight.requests.per.connection to 1 will potentially change the ordering of records because if two batches are sent to a single partition, and the first fails and is retried but the second succeeds, then the records in the second batch may appear first. Default value is "0". |
 | buffer.memory (optional) | The total bytes of memory the producer can use to buffer records waiting to be sent to the server. If records are sent faster than they can be delivered to the server the producer will block for max.block.ms after which it will throw an exception. This setting should correspond roughly to the total memory the producer will use, but is not a hard bound since not all memory the producer uses is used for buffering. Some additional memory will be used for compression (if compression is enabled) as well as for maintaining in-flight requests. Default value is "33554432". |
 | compression.type (optional) | The compression type for all data generated by the producer. The default is none (i.e. no compression). Valid values are none, gzip, snappy, or lz4. Compression is of full batches of data, so the efficacy of batching will also impact the compression ratio (more batching means better compression). |
 | batch.size (optional) | The producer will attempt to batch records together into fewer requests whenever multiple records are being sent to the same partition. This helps performance on both the client and the server. This configuration controls the default batch size in bytes. |
 | client.id (optional) | An id string to pass to the server when making requests. The purpose of this is to be able to track the source of requests beyond just ip/port by allowing a logical application name to be included in server-side request logging. |

Other possible parameters : ssl.key.password, ssl.keystore.location, ssl.keystore.password, ssl.truststore.location, ssl.truststore.password, connections.max.idle.ms, linger.ms, max.block.ms, max.request.size, partitioner.class, receive.buffer.bytes, request.timeout.ms, sasl.jaas.config, sasl.kerberos.service.name, sasl.mechanism, security.protocol, send.buffer.bytes, ssl.enabled.protocols, ssl.keystore.type, ssl.protocol, ssl.provider, ssl.truststore.type, timeout.ms, block.on.buffer.full, interceptor.classes, max.in.flight.requests.per.connection, metadata.fetch.timeout.ms, metadata.max.age.ms, metric.reporters, metrics.num.samples, metrics.sample.window.ms, reconnect.backoff.ms, retry.backoff.ms, sasl.kerberos.kinit.cmd, sasl.kerberos.min.time.before.relogin, sasl.kerberos.ticket.renew.jitter, sasl.kerberos.ticket.renew.window.factor, ssl.cipher.suites, ssl.endpoint.identification.algorithm, ssl.keymanager.algorithm, ssl.secure.random.implementation, ssl.trustmanager.algorithm
## Status Codes
* NL-KafkaProducer-01
* NL-KafkaProducer-02
* NL-KafkaProducer-03

## Contributing
Feel free to fork this repo, make changes, test locally, and create a pull request to contribute back.
