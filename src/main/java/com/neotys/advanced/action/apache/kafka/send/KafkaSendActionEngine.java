package com.neotys.advanced.action.apache.kafka.send;

import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;
import com.neotys.extensions.action.engine.Context;
import com.neotys.extensions.action.engine.SampleResult;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public final class KafkaSendActionEngine implements ActionEngine {
    private String topic;
    private String message;
    private String key;
    private String connectionName;


    private void parseParameters(List<ActionParameter> parameters)
            throws ClassNotFoundException {

        for (ActionParameter parameter : parameters) {
            String parameterName = parameter.getName().toLowerCase();

            switch (parameterName) {
                case "topic":
                    this.topic = parameter.getValue();
                    break;
                case "message":
                    this.message = parameter.getValue();
                    break;
                case "key":
                    this.key = parameter.getValue();
                    break;
                case "connectionname":
                    this.connectionName = parameter.getValue();
                    break;
                default:
                    break;
            }
        }
        //If the key is not specified set it to a random UUID
        if (this.key == null) {
            this.key = UUID.randomUUID().toString();
        }
    }

    @Override
    public SampleResult execute(Context context, List<ActionParameter> parameters) {
        final SampleResult sampleResult = new SampleResult();
        final StringBuilder requestBuilder = new StringBuilder();
        final StringBuilder responseBuilder = new StringBuilder();

        try {
            parseParameters(parameters);
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }

        appendLineToStringBuilder(requestBuilder, "Kafka message send request");
        appendLineToStringBuilder(requestBuilder, "Connection name: " + this.connectionName);
        appendLineToStringBuilder(requestBuilder, "Topic: " + this.topic);
        appendLineToStringBuilder(requestBuilder, "Key: " + this.key);
        appendLineToStringBuilder(requestBuilder, "Message: " + this.message);

        @SuppressWarnings("unchecked") final Producer<String, String> producer = (Producer<String, String>) context.getCurrentVirtualUser().get(this.connectionName);

        if (producer == null) {
            sampleResult.setError(true);
            sampleResult.setStatusCode("KAFKA-SEND-FAILED");
            sampleResult.setRequestContent(requestBuilder.toString());
            sampleResult.setResponseContent("No connection to Kafka broker!");
            return sampleResult;
        }

        sampleResult.sampleStart();

        try {
            Future<RecordMetadata> response = producer.send(new ProducerRecord<String, String>(this.topic, this.key, this.message));

            appendLineToStringBuilder(responseBuilder, "Offset : " + ((RecordMetadata) response.get()).offset());
            appendLineToStringBuilder(responseBuilder, "Timestamp : " + ((RecordMetadata) response.get()).timestamp());
            appendLineToStringBuilder(responseBuilder, "Partition : " + ((RecordMetadata) response.get()).partition());

            sampleResult.setStatusCode("KAFKA-SEND-OK");
            sampleResult.setRequestContent(requestBuilder.toString());
            sampleResult.setResponseContent(responseBuilder.toString());
        } catch (InterruptedException | ExecutionException e) {
            appendLineToStringBuilder(responseBuilder, e.getMessage());

            sampleResult.setError(true);
            sampleResult.setStatusCode("KAFKA-SEND-FAILED");
            sampleResult.setRequestContent(requestBuilder.toString());
            sampleResult.setResponseContent(responseBuilder.toString());
            return sampleResult;
        }

        sampleResult.sampleEnd();

        return sampleResult;
    }


    private void appendLineToStringBuilder(final StringBuilder sb, final String line) {
        sb.append(line).append("\n");
    }

    @Override
    public void stopExecute() {
        // TODO add code executed when the test have to stop.
    }
}