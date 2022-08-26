package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoConfiguration
@SpringBootApplication
public class ManagerApp {

	public static void main(String[] args) {
		SpringApplication.run(ManagerApp.class, args);
	}

}
