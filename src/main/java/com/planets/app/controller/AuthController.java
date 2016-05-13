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
import edu.tamu.framework.controller.CoreAuthController;
import edu.tamu.framework.model.ApiResponse;

@Controller
@ApiMapping("/auth")
public class AuthController extends CoreAuthController {
	
	@Autowired
	AppUserRepo appUserRepo;
	

	@Override
	@ApiMapping("/login")
	public ApiResponse login(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@ApiMapping("/register")
	public ApiResponse registration(String data, @Parameters Map<String, String[]> parameters) {
		ApiResponse apiResponse = null;
		
		if(	   parameters.get("email") == null 
			|| parameters.get("firstName") == null
			|| parameters.get("lastName") == null
			|| parameters.get("password") == null
		) {
			
			String error = "Missing neccessary information:";
			
			error = parameters.get("email") == null ? (error + " email") : error;
			error = parameters.get("firstName") == null ? (error + " firstName") : error;
			error = parameters.get("lastName") == null ? (error + " lastName") : error;
			error = parameters.get("password") == null ? (error + " pssword") : error;
			
			apiResponse = new ApiResponse(ERROR, error);
		} else {
			
			System.out.println(authUtility);
			AppUser user = appUserRepo.create(parameters.get("email")[0], parameters.get("firstName")[0], parameters.get("lastName")[0], authUtility.encodePassword(parameters.get("password")[0]));
			user.setRole("ROLE_USER");
			appUserRepo.save(user);
			apiResponse = new ApiResponse(SUCCESS, "The email " +parameters.get("email")[0]+" was registered.", user);
		}
		
		return apiResponse;
	}

}
