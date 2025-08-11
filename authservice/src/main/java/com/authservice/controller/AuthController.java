package com.authservice.controller;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.authservice.entity.User;
import com.authservice.payload.APIResponse;
import com.authservice.payload.LoginDto;
import com.authservice.payload.UserDto;
import com.authservice.repository.UserRepository;
import com.authservice.service.AuthService;
import com.authservice.service.JWTService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
	
	private final AuthService authService ;
	private final AuthenticationManager authenticationManager;
	private final JWTService jwtService;
	private final UserRepository userRepository;

	public AuthController(AuthService authService,AuthenticationManager authenticationManager, JWTService jwtService, UserRepository userRepository ) {
		this.authService = authService;
		this.authenticationManager=authenticationManager;
		this.jwtService=jwtService;
		this.userRepository=userRepository;
	}
	
	@PostMapping("/register")
	public ResponseEntity<APIResponse<String>> register(@RequestBody UserDto dto){	
		APIResponse<String> response =authService.register(dto);
		return new ResponseEntity<>(response,HttpStatusCode.valueOf(response.getStatus()));	
	}
	
	
	@PostMapping("/login")
	public ResponseEntity<APIResponse<String>> login(@RequestBody LoginDto dto){
		
		APIResponse<String> response = new APIResponse<>();
		
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
		
		try {
			  Authentication authentication = authenticationManager.authenticate(token);
			  
			  if(authentication.isAuthenticated()) {
				  
				  //If Authentication is sucessfull than we have to generate Token
				  
				  String jwtToken = jwtService.generateToken(dto.getUsername(),authentication.getAuthorities().iterator().next().getAuthority());
				  
				  response.setMessage("Login Successful");
		            response.setStatus(200);
		            response.setData(jwtToken);  // return JWT
		            return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatus()));
			  }
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		response.setMessage("Failed");
	    response.setStatus(401);
	    response.setData("Unauthorized");
	    return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatus()));
		
		
	}
	
	@GetMapping("/get-user")
	public User getUserByUserName(@RequestParam String username) {
		 User user = userRepository.findByUsername(username);
		 return user;
		 
	}

}
