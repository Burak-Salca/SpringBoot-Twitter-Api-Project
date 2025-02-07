package com.example.SpringBoot_Twitter_Api_Project;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
		title = "Twitter API",
		version = "1.0",
		description = "Twitter Clone API Documentation"
))
public class SpringBootTwitterApiProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootTwitterApiProjectApplication.class, args);
	}

}
