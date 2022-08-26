package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoConfiguration
@SpringBootApplication
public class WorkerApp {

	public static void main(String[] args) {
		SpringApplication.run(WorkerApp.class, args);
	}

}
