package com.neotys.advanced.action.apache.kafka.disconnect;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class KafkaDisconnectActionTest {
	@Test
	public void shouldReturnType() {
		final KafkaDisconnectAction action = new KafkaDisconnectAction();
		assertEquals("KafkaDisconnect", action.getType());
	}

}
