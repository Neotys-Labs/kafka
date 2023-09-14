package com.neotys.advanced.action.apache.kafka.send;

import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;
import com.neotys.extensions.action.engine.Context;
import com.neotys.extensions.action.engine.SampleResult;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.neotys.advanced.action.apache.kafka.send.KafkaSendActionEngine.KafkaHeaderParser.parseHeadersOption;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableSet;
import static java.util.stream.Collectors.toSet;

public final class KafkaSendActionEngine implements ActionEngine {
    private String topic;
    private String message;
    private String key;
    private String connectionName;
    private Set<Header> headers;


    private void parseParameters(List<ActionParameter> parameters) {

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
                case "headers":
                    this.headers = parseHeadersOption(parameter.getValue());
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

        parseParameters(parameters);

        appendLineToStringBuilder(requestBuilder, "Kafka message send request");
        appendLineToStringBuilder(requestBuilder, "Connection name: " + this.connectionName);
        appendLineToStringBuilder(requestBuilder, "Topic: " + this.topic);
        appendLineToStringBuilder(requestBuilder, "Key: " + this.key);
        appendLineToStringBuilder(requestBuilder, "Message: " + this.message);
        appendLineToStringBuilder(requestBuilder, "Headers: " + this.headers);

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
            ProducerRecord<String, String> record = new ProducerRecord<>(this.topic, null, null, this.key, this.message, this.headers);
            Future<RecordMetadata> response = producer.send(record);
            appendLineToStringBuilder(responseBuilder, "Offset : " + response.get().offset());
            appendLineToStringBuilder(responseBuilder, "Timestamp : " + response.get().timestamp());
            appendLineToStringBuilder(responseBuilder, "Partition : " + response.get().partition());

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

    static class KafkaHeaderParser {

        private static final Pattern KEY_PAIR_PATTERN = Pattern.compile("([^,]+=[^,]*)");

        private KafkaHeaderParser() {
            // no-op
        }

        static Set<Header> parseHeadersOption(final String headerOption) {
            if (headerOption == null || headerOption.isEmpty()) {
                return emptySet();
            }
            // Map will take care of duplicate keys.
            final Map<String, String> headerMap = new TreeMap<>();
            // Remove all escaped characters including the escape character itself while preserving the original length.
            final String normalized = headerOption.replaceAll("\\\\[=,]", "  ");

            final Matcher matcher = KEY_PAIR_PATTERN.matcher(normalized);
            while (matcher.find()) {
                int from = matcher.start();
                int to = matcher.end();
                int splitAt = normalized.indexOf('=', from);
                headerMap.put(
                        // Remove escapes from the key and value.
                        headerOption.substring(from, splitAt).replaceAll("\\\\([=,])|\\\\$", "$1").trim(),
                        headerOption.substring(splitAt + 1, to).replaceAll("\\\\([=,])|\\\\$", "$1").trim()
                );
            }

            //transform the map into a set of RecordHeader
            return unmodifiableSet(headerMap.entrySet().stream()
                    .filter(entry -> !entry.getKey().isEmpty()) //skip entries with empty keys
                    .map(entry -> new RecordHeader(entry.getKey(), entry.getValue().getBytes(UTF_8)) {
                        @Override
                        public String toString() {
                            return String.format("%s=%s", key(), new String(value(), UTF_8));
                        }
                    })
                    .collect(toSet()));
        }
    }

}