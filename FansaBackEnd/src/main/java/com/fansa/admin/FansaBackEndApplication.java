package com.fansa.admin;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.fansa.common","com.fansa.admin"})
public class FansaBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(FansaBackEndApplication.class, args);
	}

}
