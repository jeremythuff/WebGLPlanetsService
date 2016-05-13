package com.planets.app.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.planets.app.model.AppUser;
import com.planets.app.model.repo.AppUserRepo;

import static edu.tamu.framework.enums.ApiResponseType.SUCCESS;
import static edu.tamu.framework.enums.ApiResponseType.ERROR;

import edu.tamu.framework.aspect.annotation.ApiMapping;
import edu.tamu.framework.aspect.annotation.Parameters;
import edu.tamu.framework.model.ApiResponse;

@Controller
@ApiMapping("/auth")
public class AuthController {
	
	@Autowired
	AppUserRepo appUserRepo;
	
	@ApiMapping("/register")
	public ApiResponse register(@Parameters Map<String, String[]> parameters) {
		
		ApiResponse apiResponse = null;
		
		if(	   parameters.get("email") == null 
			|| parameters.get("firstName") == null
			|| parameters.get("lastName") == null
			|| parameters.get("password") == null
		) {
			apiResponse = new ApiResponse(ERROR, "Missing neccessary information.");
		} else {
			AppUser user = appUserRepo.create(parameters.get("email")[0], parameters.get("firstName")[0], parameters.get("lastName")[0], parameters.get("password")[0]);
			user.setRole("ROLE_USER");
			appUserRepo.save(user);
			apiResponse = new ApiResponse(SUCCESS, "The email " +parameters.get("email")[0]+" was registered.", user);
		}
		
		return apiResponse;
		
	}
	
	@ApiMapping("/login")
	public ApiResponse login() {
		return new ApiResponse(SUCCESS);
	}

}
