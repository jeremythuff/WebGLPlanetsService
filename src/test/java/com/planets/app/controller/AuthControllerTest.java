package com.planets.app.controller;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.planets.app.model.AppUser;
import com.planets.app.model.repo.AppUserRepo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import edu.tamu.framework.enums.ApiResponseType;
import edu.tamu.framework.model.ApiResponse;
import edu.tamu.framework.model.Credentials;
import edu.tamu.framework.util.AuthUtility;
import edu.tamu.framework.util.JwtUtility;

public class AuthControllerTest extends AbstractControllerTest {
	
    
    private Credentials TEST_CREDENTIALS = new Credentials();
	
	@Spy
	private ObjectMapper objectMapper;
	
	@Spy @InjectMocks
	private AuthUtility authUtility;
	
	@Spy
	private BCryptPasswordEncoder passwordEncoder;
	
	@Spy @InjectMocks
	private JwtUtility jwtUtility;
	
	private static final String SECRET_PROPERTY_NAME = "secret";
	private static final String SECRET_VALUE = "verysecretsecret";
	
	private static final String JWT_SECRET_KEY_PROPERTY_NAME = "secret_key";
	private static final String JWT_SECRET_KEY_VALUE = "verysecretsecret";
	
	private static final String JWT_EXPIRATION_PROPERTY_NAME = "expiration";
	private static final Long JWT_EXPIRATION_VALUE = 120000L;
	
	private static final String[] TEST_USER_1_EMAIL = {"testUser1@domain.tld"};
	private static final String[] TEST_USER_1_FIRST_NAME = {"Test 1"};
	private static final String[] TEST_USER_1_LAST_NAME = {"User 1"};
	private static final String[] TEST_USER_1_PASSWORD = {"iamtestuser1"};
	
	private static final String[] TEST_USER_2_EMAIL = {"testUser2@domain.tld"};
	private static final String[] TEST_USER_2_FIRST_NAME = {"Test 2"};
	private static final String[] TEST_USER_2_LAST_NAME = {"User 2"};
	private static final String[] TEST_USER_2_PASSWORD = {"iamtestuser2"};
	
	private AppUser TEST_USER_1 = new AppUser(TEST_USER_1_EMAIL[0], TEST_USER_1_FIRST_NAME[0], TEST_USER_1_LAST_NAME[0], TEST_USER_1_PASSWORD[0]);
	private AppUser TEST_USER_2 = new AppUser(TEST_USER_2_EMAIL[0], TEST_USER_2_FIRST_NAME[0], TEST_USER_2_LAST_NAME[0], TEST_USER_2_PASSWORD[0]);
	
	@Mock
    private AppUserRepo appUserRepo;
	
	@InjectMocks
    private AuthController authController;
	
	private static List<AppUser> mockUsers;
	
	public AppUser findByEmail(String email) {    	
        for(AppUser user : mockUsers) {
            if(user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }
	
	private AppUser updateUser(AppUser updatedUser) {
        for(AppUser user : mockUsers) {
            if(user.getEmail().equals(updatedUser.getEmail())) {
            	user.setEmail(updatedUser.getEmail());
            	user.setFirstName(updatedUser.getFirstName());
            	user.setLastName(updatedUser.getLastName());
            	user.setPassword(updatedUser.getPassword());
            	user.setRole(updatedUser.getRole());
                return user;
            }
        }
        return null;
    }
	
	@Before
	public void setup() {
		
		MockitoAnnotations.initMocks(this);
		
		mockUsers = Arrays.asList(new AppUser[] {TEST_USER_1, TEST_USER_2});
		
    	ReflectionTestUtils.setField(authUtility, SECRET_PROPERTY_NAME, SECRET_VALUE);
    	
    	ReflectionTestUtils.setField(jwtUtility, JWT_SECRET_KEY_PROPERTY_NAME, JWT_SECRET_KEY_VALUE);
    	    	
    	ReflectionTestUtils.setField(jwtUtility, JWT_EXPIRATION_PROPERTY_NAME, JWT_EXPIRATION_VALUE);
    	
    	TEST_CREDENTIALS.setFirstName(TEST_USER_1_FIRST_NAME[0]);
    	TEST_CREDENTIALS.setLastName(TEST_USER_1_LAST_NAME[0]);
    	TEST_CREDENTIALS.setEmail(TEST_USER_1_EMAIL[0]);
		
		Mockito.when(appUserRepo.findAll()).thenReturn(mockUsers);
		
		Mockito.when(appUserRepo.create(any(String.class), any(String.class), any(String.class), any(String.class))).then(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return appUserRepo.save(new AppUser((String) invocation.getArguments()[0], 
                							  (String) invocation.getArguments()[1], 
                							  (String) invocation.getArguments()[2], 
                							  (String) invocation.getArguments()[3]));
            }}
        );
		
		Mockito.when(appUserRepo.save(any(AppUser.class))).then(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return updateUser((AppUser) invocation.getArguments()[0]);
            }}
        );
		
		Mockito.when(appUserRepo.findByEmail(any(String.class))).then(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {            	            	
                return findByEmail((String) invocation.getArguments()[0]);
            }}
        );
		
	}
	
	@Test
    public void testLogin() throws Exception {

    	testRegister();
    	
    	Map<String, String> data = new HashMap<String, String>();
    	data.put("email", TEST_USER_1_EMAIL[0]);
    	data.put("password", TEST_USER_1_PASSWORD[0]);
    	
    	ApiResponse response = authController.login(objectMapper.convertValue(data, JsonNode.class).toString());
    	    	
    	assertEquals(response.getMeta().getMessage(), ApiResponseType.SUCCESS, response.getMeta().getType());
    }
	
	@Test
    public void testRegister() throws Exception {
    	
    	Map<String, String[]> parameters = new HashMap<String, String[]>();
    	
    	parameters.put("email", TEST_USER_1_EMAIL);
    	parameters.put("password", TEST_USER_1_PASSWORD);
    	
		ApiResponse response = authController.registration("test", parameters);
		
    	AppUser user = (AppUser) response.getPayload().get("AppUser");
    	
    	assertEquals(ApiResponseType.SUCCESS, response.getMeta().getType());

    	assertEquals(TEST_USER_1_EMAIL[0], user.getEmail());
    	assertEquals(true, authUtility.validatePassword(TEST_USER_1_PASSWORD[0], user.getPassword()));
    }

}
