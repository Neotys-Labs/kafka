package com.neotys.advanced.action.apache.kafka.disconnect;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class KafkaDisconnectActionTest {
	@Test
	void shouldReturnType() {
		final KafkaDisconnectAction action = new KafkaDisconnectAction();
		assertEquals("KafkaDisconnect", action.getType());
	}

}
