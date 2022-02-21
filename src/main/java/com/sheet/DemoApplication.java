package com.sheet;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) throws GeneralSecurityException, IOException {
		SpringApplication.run(DemoApplication.class, args);
	}
}
