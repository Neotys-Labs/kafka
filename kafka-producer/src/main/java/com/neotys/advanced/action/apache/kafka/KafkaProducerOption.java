package com.neotys.advanced.action.apache.kafka;

import com.neotys.action.argument.ArgumentValidator;
import com.neotys.action.argument.DefaultArgumentValidator;
import com.neotys.action.argument.Option;
import com.neotys.extensions.action.ActionParameter;

enum KafkaProducerOption implements Option {
    Topic("topic", Option.OptionalRequired.Required, Option.AppearsByDefault.True, ActionParameter.Type.TEXT, "", "The topic to send the message to.", DefaultArgumentValidator.NON_EMPTY),
    Message("message", Option.OptionalRequired.Required, Option.AppearsByDefault.True, ActionParameter.Type.TEXT, "", "The message to send.", DefaultArgumentValidator.NON_EMPTY),
    DestinationName("destination", Option.OptionalRequired.Required, Option.AppearsByDefault.True, ActionParameter.Type.TEXT, "", "The name of the destination server.", DefaultArgumentValidator.NON_EMPTY),
    DestinationPort("port", Option.OptionalRequired.Optional, Option.AppearsByDefault.False, ActionParameter.Type.TEXT, "9092", "The port of the destination server.", DefaultArgumentValidator.INTEGER_VALIDATOR),
    Key("key.serializer", Option.OptionalRequired.Optional, Option.AppearsByDefault.False, ActionParameter.Type.TEXT, "org.apache.kafka.common.serialization.StringSerializer", "Serializer class for key that implements the Serializer interface.", DefaultArgumentValidator.NON_EMPTY),
    Value("value.serializer", Option.OptionalRequired.Optional, Option.AppearsByDefault.False, ActionParameter.Type.TEXT, "org.apache.kafka.common.serialization.StringSerializer", "Serializer class for value that implements the Serializer interface.", DefaultArgumentValidator.NON_EMPTY),
    Acks("acks", Option.OptionalRequired.Optional, Option.AppearsByDefault.False, ActionParameter.Type.TEXT, "1", "The number of acknowledgments the producer requires the leader to have received before considering a request complete. This controls the durability of records that are sent. The following settings are allowed:\n\t- acks=0 If set to zero then the producer will not wait for any acknowledgment from the server at all. The record will be immediately added to the socket buffer and considered sent. No guarantee can be made that the server has received the record in this case, and the retries configuration will not take effect (as the client won't generally know of any failures). The offset given back for each record will always be set to -1.\n\t- acks=1 This will mean the leader will write the record to its local log but will respond without awaiting full acknowledgement from all followers. In this case should the leader fail immediately after acknowledging the record but before the followers have replicated it then the record will be lost.\n\t- acks=all This means the leader will wait for the full set of in-sync replicas to acknowledge the record. This guarantees that the record will not be lost as long as at least one in-sync replica remains alive. This is the strongest available guarantee. This is equivalent to the acks=-1 setting.", DefaultArgumentValidator.NON_EMPTY),
    Retries("retries", Option.OptionalRequired.Optional, Option.AppearsByDefault.False, ActionParameter.Type.TEXT, "0", "Setting a value greater than zero will cause the client to resend any record whose send fails with a potentially transient error. Note that this retry is no different than if the client resent the record upon receiving the error. Allowing retries without setting max.in.flight.requests.per.connection to 1 will potentially change the ordering of records because if two batches are sent to a single partition, and the first fails and is retried but the second succeeds, then the records in the second batch may appear first.", DefaultArgumentValidator.INTEGER_VALIDATOR),
    BufferMemory("buffer.memory", Option.OptionalRequired.Optional, Option.AppearsByDefault.False, ActionParameter.Type.TEXT, "33554432", "The total bytes of memory the producer can use to buffer records waiting to be sent to the server. If records are sent faster than they can be delivered to the server the producer will block for max.block.ms after which it will throw an exception. This setting should correspond roughly to the total memory the producer will use, but is not a hard bound since not all memory the producer uses is used for buffering. Some additional memory will be used for compression (if compression is enabled) as well as for maintaining in-flight requests.", DefaultArgumentValidator.LONG_VALIDATOR),
    CompressionType("compression.type", Option.OptionalRequired.Optional, Option.AppearsByDefault.False, ActionParameter.Type.TEXT, "", "The compression type for all data generated by the producer. The default is none (i.e. no compression). Valid values are none, gzip, snappy, or lz4. Compression is of full batches of data, so the efficacy of batching will also impact the compression ratio (more batching means better compression).", DefaultArgumentValidator.NON_EMPTY),
    BatchSize("batch.size", Option.OptionalRequired.Optional, Option.AppearsByDefault.False, ActionParameter.Type.TEXT, "", "The producer will attempt to batch records together into fewer requests whenever multiple records are being sent to the same partition. This helps performance on both the client and the server. This configuration controls the default batch size in bytes.", DefaultArgumentValidator.INTEGER_VALIDATOR),
    ClientId("client.id", Option.OptionalRequired.Optional, Option.AppearsByDefault.False, ActionParameter.Type.TEXT, "", "An id string to pass to the server when making requests. The purpose of this is to be able to track the source of requests beyond just ip/port by allowing a logical application name to be included in server-side request logging.", DefaultArgumentValidator.NON_EMPTY);

    private final String name;
    private final Option.OptionalRequired optionalRequired;
    private final Option.AppearsByDefault appearsByDefault;
    private final ActionParameter.Type type;
    private final String defaultValue;
    private final String description;
    private final ArgumentValidator argumentValidator;

    KafkaProducerOption(String name, Option.OptionalRequired optionalRequired, Option.AppearsByDefault appearsByDefault, ActionParameter.Type type, String defaultValue, String description, ArgumentValidator argumentValidator) {
        this.name = name;
        this.optionalRequired = optionalRequired;
        this.appearsByDefault = appearsByDefault;
        this.type = type;
        this.defaultValue = defaultValue;
        this.description = description;
        this.argumentValidator = argumentValidator;
    }

    public String getName() {
        return this.name;
    }

    public Option.OptionalRequired getOptionalRequired() {
        return this.optionalRequired;
    }

    public Option.AppearsByDefault getAppearsByDefault() {
        return this.appearsByDefault;
    }

    public ActionParameter.Type getType() {
        return this.type;
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public String getDescription() {
        return this.description;
    }

    public ArgumentValidator getArgumentValidator() {
        return this.argumentValidator;
    }

}