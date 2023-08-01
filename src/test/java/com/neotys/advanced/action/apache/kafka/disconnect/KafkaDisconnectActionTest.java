package com.neotys.advanced.action.apache.kafka.disconnect;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class KafkaDisconnectActionTest {
	@Test
	public void shouldReturnType() {
		final KafkaDisconnectAction action = new KafkaDisconnectAction();
		assertEquals("KafkaDisconnect", action.getType());
	}

}
