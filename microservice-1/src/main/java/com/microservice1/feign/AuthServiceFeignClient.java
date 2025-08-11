package com.microservice1.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.microservice1.dto.User;





@FeignClient("AUTHSERVICE")
public interface AuthServiceFeignClient {
	

	@GetMapping("/api/v1/auth/get-user")
	User getUserByUserName(
	    @RequestParam String username,
	    @RequestHeader("Authorization") String token
	);

	

	
}
