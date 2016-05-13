package com.planets.app.controller;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.planets.app.model.AppUser;
import com.planets.app.model.repo.AppUserRepo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import edu.tamu.framework.enums.ApiResponseType;
import edu.tamu.framework.model.ApiResponse;

public class AuthControllerTest extends AbstractControllerTest {
	
	public static final String[] TEST_USER_1_EMAIL = {"testUser1@domain.tld"};
	public static final String[] TEST_USER_1_FIRST_NAME = {"Test 1"};
	public static final String[] TEST_USER_1_LAST_NAME = {"User 1"};
	public static final String[] TEST_USER_1_PASSWORD = {"iamtestuser1"};
	
	public static final String[] TEST_USER_2_EMAIL = {"testUser2@domain.tld"};
	public static final String[] TEST_USER_2_FIRST_NAME = {"Test 2"};
	public static final String[] TEST_USER_2_LAST_NAME = {"User 2"};
	public static final String[] TEST_USER_2_PASSWORD = {"iamtestuser2"};
	
	protected AppUser TEST_USER_1 = new AppUser(TEST_USER_1_EMAIL[0], TEST_USER_1_FIRST_NAME[0], TEST_USER_1_LAST_NAME[0], TEST_USER_1_PASSWORD[0]);
	protected AppUser TEST_USER_2 = new AppUser(TEST_USER_2_EMAIL[0], TEST_USER_2_FIRST_NAME[0], TEST_USER_2_LAST_NAME[0], TEST_USER_2_PASSWORD[0]);
	
	@Mock
    private AppUserRepo appUserRepo;
	
	@InjectMocks
    private AuthController authController;
	
	private static List<AppUser> mockUsers;
	
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
		
	}
	
	@Test
    public void testRegister() throws Exception {
    	
    	Map<String, String[]> parameters = new HashMap<String, String[]>();
    	
    	parameters.put("email", TEST_USER_1_EMAIL);
    	parameters.put("firstName", TEST_USER_1_FIRST_NAME);
    	parameters.put("lastName", TEST_USER_1_LAST_NAME);
    	parameters.put("password", TEST_USER_1_PASSWORD);
    	
		ApiResponse response = authController.register(parameters);
		
    	AppUser user = (AppUser) response.getPayload().get("AppUser");
    	
    	assertEquals(ApiResponseType.SUCCESS, response.getMeta().getType());
    	
    	assertEquals(TEST_USER_1_FIRST_NAME[0], user.getFirstName());
    	assertEquals(TEST_USER_1_LAST_NAME[0], user.getLastName());
    	assertEquals(TEST_USER_1_EMAIL[0], user.getEmail());
    	assertEquals(TEST_USER_1_PASSWORD[0], user.getPassword());
    }

}
