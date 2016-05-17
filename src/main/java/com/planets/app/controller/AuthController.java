package com.planets.app.controller;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.planets.app.model.AppUser;
import com.planets.app.model.repo.AppUserRepo;

import static edu.tamu.framework.enums.ApiResponseType.SUCCESS;
import static edu.tamu.framework.enums.ApiResponseType.ERROR;
import edu.tamu.framework.aspect.annotation.ApiMapping;
import edu.tamu.framework.aspect.annotation.Data;
import edu.tamu.framework.aspect.annotation.Parameters;
import edu.tamu.framework.controller.CoreAuthController;
import edu.tamu.framework.model.ApiResponse;

@Controller
@ApiMapping("/auth")
public class AuthController extends CoreAuthController {
	
	@Autowired
	AppUserRepo appUserRepo;
	
	@ApiMapping(value="/login", method=RequestMethod.POST)
	public ApiResponse login(@Parameters Map<String, String[]> parameters) {
		    
	        String email = parameters.get("email")[0];
	        String password = parameters.get("password")[0];
	        
	        AppUser user = appUserRepo.findByEmail(email);
	        
	        if(user == null) {
	            logger.debug("No user found with email " + email + "!");
	            return new ApiResponse(ERROR, "No user found with email " + email + "!");
	        }
	        
	        if(!authUtility.validatePassword(password, user.getPassword())) {
	            logger.debug("Authentication failed!");
	            return new ApiResponse(ERROR, "Authentication failed!");
	        }
	        
	        try {
	        	Map<String, String> userMap = new HashMap<String, String>();
	        	userMap.put("lastName", user.getLastName());
	        	userMap.put("firstName", user.getFirstName());
	        	userMap.put("uin", String.valueOf(user.getUin()));
	        	userMap.put("email", user.getEmail());
	            return new ApiResponse(SUCCESS, jwtUtility.makeToken(userMap));
	        } catch (InvalidKeyException | JsonProcessingException | NoSuchAlgorithmException | IllegalStateException | UnsupportedEncodingException e) {
	            logger.debug("Unable to generate token!");
	            return new ApiResponse(ERROR, "Unable to generate token!");
	        }
	}
	
	@ApiMapping(value="/register", method=RequestMethod.POST)
	@Transactional
	public ApiResponse registration(@Parameters Map<String, String[]> parameters) {
		ApiResponse apiResponse = null;
		
		if(	   parameters.get("email") == null 
			|| parameters.get("password") == null
		) {
			
			String error = "Missing neccessary information:";
			
			error = parameters.get("email") == null ? (error + " email") : error;
			error = parameters.get("password") == null ? (error + " pssword") : error;
			
			apiResponse = new ApiResponse(ERROR, error);
		} else {
			
			System.out.println(authUtility);
			AppUser user = appUserRepo.create(parameters.get("email")[0], "", "", authUtility.encodePassword(parameters.get("password")[0]));
			user.setRole("ROLE_USER");
			appUserRepo.save(user);
			apiResponse = new ApiResponse(SUCCESS, "The email " +parameters.get("email")[0]+" was registered.", user);
		}
		
		return apiResponse;
	}
	
    public ApiResponse login(@Data String data) {return null;}
	public ApiResponse registration(String data, @Parameters Map<String, String[]> parameters) {return null;}

}
