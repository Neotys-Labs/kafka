package com.neotys.advanced.action.apache.kafka.connect;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KafkaConnectActionTest {
    @Test
    public void shouldReturnType() {
        final KafkaConnectAction action = new KafkaConnectAction();
        assertEquals("KafkaConnect", action.getType());
    }

}
