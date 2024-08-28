package com.poc.reactive.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.reactive.demo.services.FileSystemService;
import com.poc.reactive.demo.services.ImageHandler;
import com.poc.reactive.demo.services.ImageResizerService;
import com.poc.reactive.demo.services.WaterMarkAdderService;

@Configuration
public class BeansConfig {
	
	@Profile("resizer")
	@Bean
	ImageHandler resizerImageHandler(FileSystemService service) {
	    return new ImageResizerService(service);
	}

	@Profile("watermark")
	@Bean
	ImageHandler watermarkImageHandler(FileSystemService service) {
	    return new WaterMarkAdderService(service); 
	}
	
	@Bean
	ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

}
