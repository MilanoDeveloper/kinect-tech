package com.kinect.orchestrator;

import tools.jackson.databind.JacksonModule;
import tools.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class OrchestratorApplicationTests {

	@Autowired
	private JacksonModule localDateModule;

	@Test
	void contextLoads() {
	}

	@Test
	void serializesAndDeserializesLocalDateAsDayMonthYear() throws Exception {
		LocalDate date = LocalDate.of(2026, 10, 3);
		JsonMapper jsonMapper = JsonMapper.builder().addModule(localDateModule).build();

		assertEquals("\"03102026\"", jsonMapper.writeValueAsString(date));
		assertEquals(date, jsonMapper.readValue("\"03102026\"", LocalDate.class));
	}
}
