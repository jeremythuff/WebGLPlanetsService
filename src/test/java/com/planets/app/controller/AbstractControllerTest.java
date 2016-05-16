package com.planets.app.controller;

import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.web.WebAppConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.planets.app.WebServerInit;

import edu.tamu.framework.model.Credentials;
import edu.tamu.framework.util.AuthUtility;
import edu.tamu.framework.util.JwtUtility;

@WebAppConfiguration
@SpringApplicationConfiguration(classes = { WebServerInit.class })
public abstract class AbstractControllerTest {

	
	
}
