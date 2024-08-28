package com.poc.reactive.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class DemoApplication {

	@Profile("unset")
	@Bean
	CommandLineRunner usage() {
		throw new IllegalArgumentException(
				"a profile must be set to start this service: profiles available -> resizer or watermark");
	}

	@Profile("!unset")
	@Bean
	CommandLineRunner set() {
		return new RabbitAmqpTutorialsRunner();
	}

	
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
