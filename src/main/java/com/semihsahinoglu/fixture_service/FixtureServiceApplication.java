package com.semihsahinoglu.fixture_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FixtureServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FixtureServiceApplication.class, args);
	}

}
