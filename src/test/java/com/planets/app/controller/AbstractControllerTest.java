package com.planets.app.controller;

import org.mockito.Spy;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.planets.app.WebServerInit;

@WebAppConfiguration
@SpringApplicationConfiguration(classes = { WebServerInit.class })
public abstract class AbstractControllerTest {

	@Spy
	protected ObjectMapper objectMapper;
	
}
