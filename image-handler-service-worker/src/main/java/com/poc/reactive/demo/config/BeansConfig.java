package com.poc.reactive.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.reactive.demo.services.ITransformationJob;
import com.poc.reactive.demo.services.impl.TransformationResize;
import com.poc.reactive.demo.services.impl.TransformationWaterMark;

@Configuration
public class BeansConfig {

	@Profile("resizer")
	@Bean
	ITransformationJob resizerImageHandler() {
		return new TransformationResize();
	}

	@Profile("watermark")
	@Bean
	ITransformationJob watermarkImageHandler() {
		return new TransformationWaterMark();
	}

	@Bean
	ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

}
