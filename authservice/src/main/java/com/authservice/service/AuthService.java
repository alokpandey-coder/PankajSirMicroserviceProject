package com.authservice.service;

import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.authservice.entity.User;
import com.authservice.payload.APIResponse;
import com.authservice.payload.UserDto;
import com.authservice.repository.UserRepository;

@Service
public class AuthService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncode;

	public AuthService (UserRepository userRepository,PasswordEncoder passwordEncode) {
		this.userRepository = userRepository;
		this.passwordEncode=passwordEncode;
	}
	

	public APIResponse<String> register(@RequestBody UserDto dto) {
		
		APIResponse<String> response = new APIResponse<>();
		
		//Check UserName is Already Present or Not
		if(userRepository.existsByUsername(dto.getUsername())) {
			
			response.setMessage("Registration Failed !!!!!!!!");
			response.setStatus(500);
			response.setData("User with this userName is already  Exists");
			return response;
		}
		
		//Check Email is Already Present or Not
		
		if(userRepository.existsByEmail(dto.getEmail())) {
			response.setMessage("Registration Failed !!!!!!!!");
			response.setStatus(500);
			response.setData("User with this Email is already  Exists");
			return response;
		}
		
		//Encode The Password by Help of SpringSecurity
		
		String encryptedPassword=passwordEncode.encode(dto.getPassword());
		
		User user = new User();
		BeanUtils.copyProperties(dto,user);
		user.setPassword(encryptedPassword);
		User savedUser = userRepository.save(user);
		
		   response.setMessage("Registration Completed");
			response.setStatus(201);
			response.setData("User has been Registered");
			return response;
		
		
	}

}
