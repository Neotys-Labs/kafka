package com.neotys.advanced.action.apache.kafka.connect;

import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;
import com.neotys.extensions.action.engine.Context;
import com.neotys.extensions.action.engine.SampleResult;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.List;
import java.util.Properties;

import static java.lang.String.format;

public final class KafkaConnectActionEngine implements ActionEngine {
    private Properties props;
    private String connectionName;
    private void parseParameters(List<ActionParameter> parameters) {
        this.props = new Properties();
        this.props.put("key.serializer", StringSerializer.class);
        this.props.put("value.serializer", StringSerializer.class);

        for (ActionParameter parameter : parameters) {
            String parameterName = parameter.getName().toLowerCase();

            switch (parameterName) {
                case "connectionname":
                    this.connectionName = parameter.getValue();
                    break;
                default:
                    this.props.put(parameterName, parameter.getValue());
                    break;
            }
        }
    }

    @Override
    public SampleResult execute(Context context, List<ActionParameter> parameters) {
        final SampleResult sampleResult = new SampleResult();
        final StringBuilder requestBuilder = new StringBuilder();

        parseParameters(parameters);


        appendLineToStringBuilder(requestBuilder, "Kafka connect request");
        appendLineToStringBuilder(requestBuilder, "Connection name: " + this.connectionName);
        appendLineToStringBuilder(requestBuilder, "Kafka properties:");
        props.stringPropertyNames().forEach(key -> appendLineToStringBuilder(requestBuilder, format("%s : %s ", key, props.getProperty(key))));

        sampleResult.sampleStart();

        try {
            Producer<String, String> producer = new KafkaProducer<>(this.props);
            //Put the producer in the Context of a VU
            context.getCurrentVirtualUser().put(this.connectionName, producer);

            sampleResult.setStatusCode("KAFKA-CONNECT-OK");
            sampleResult.setRequestContent(requestBuilder.toString());
            sampleResult.setResponseContent("Kafka producer created successfully");
        } catch (RuntimeException runtimeException) {
            sampleResult.setError(true);
            sampleResult.setStatusCode("KAFKA-CONNECT-FAILED");
            sampleResult.setRequestContent(requestBuilder.toString());
            sampleResult.setResponseContent("Connection to Kafka broker failed!\n" + runtimeException.getMessage());
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