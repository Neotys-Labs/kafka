package com.neotys.advanced.action.apache.kafka.send;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class KafkaSendActionTest {
	@Test
	public void shouldReturnType() {
		final KafkaSendAction action = new KafkaSendAction();
		assertEquals("KafkaSend", action.getType());
	}

}
