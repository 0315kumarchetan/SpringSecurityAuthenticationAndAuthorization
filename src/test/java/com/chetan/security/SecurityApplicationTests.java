package com.chetan.security;

import com.chetan.security.entity.User;
import com.chetan.security.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SecurityApplicationTests {

	/*@Autowired
	JwtService jwtService;*/
	@Test
	void contextLoads() {

		/*User user = new User(2L,"test@gmail.com","pass","chetan");

		String token = jwtService.generateToken(user);

		System.out.println(token);

		Long userId = jwtService.getUserIdFromToken(token);

		System.out.println(userId);*/
	}

}
