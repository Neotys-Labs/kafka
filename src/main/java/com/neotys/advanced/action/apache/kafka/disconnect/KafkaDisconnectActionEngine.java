package com.neotys.advanced.action.apache.kafka.disconnect;

import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;
import com.neotys.extensions.action.engine.Context;
import com.neotys.extensions.action.engine.SampleResult;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.errors.InterruptException;

import java.util.List;

public final class KafkaDisconnectActionEngine implements ActionEngine {
    private String connectionName;

    private void parseParameters(List<ActionParameter> parameters)
            throws ClassNotFoundException {

        for (ActionParameter parameter : parameters) {
            String parameterName = parameter.getName().toLowerCase();

            switch (parameterName) {
                case "connectionname":
                    this.connectionName = parameter.getValue();
                    break;
                default:
                    break;
            }
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

        appendLineToStringBuilder(requestBuilder, "Kafka Disconnect request");
        appendLineToStringBuilder(requestBuilder, "Connection name: " + this.connectionName);

        @SuppressWarnings("unchecked") final Producer<String, String> producer = (Producer<String, String>) context.getCurrentVirtualUser().get(this.connectionName);

        if (producer == null) {
            sampleResult.setError(true);
            sampleResult.setStatusCode("KAFKA-DISCONNECT-FAILED");
            sampleResult.setResponseContent("No connection to Kafka broker found!");
            return sampleResult;
        }

        sampleResult.sampleStart();

        try {
            producer.close();
            context.getCurrentVirtualUser().remove(this.connectionName);

            sampleResult.setStatusCode("KAFKA-DISCONNECT-OK");
            appendLineToStringBuilder(responseBuilder, "Successfully closed the Kafka producer");

            sampleResult.setRequestContent(requestBuilder.toString());
            sampleResult.setResponseContent(responseBuilder.toString());
        } catch (InterruptException e) {
            sampleResult.setError(true);
            sampleResult.setStatusCode("KAFKA-DISCONNECT-FAILED");

            appendLineToStringBuilder(responseBuilder, "Failed to close the Kafka producer!");
            appendLineToStringBuilder(responseBuilder, e.getMessage());

            sampleResult.setRequestContent(requestBuilder.toString());
            sampleResult.setResponseContent(responseBuilder.toString());
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