package com.neotys.advanced.action.apache.kafka.connect;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class KafkaConnectActionTest {
	@Test
	public void shouldReturnType() {
		final KafkaConnectAction action = new KafkaConnectAction();
		assertEquals("KafkaConnect", action.getType());
	}

}
