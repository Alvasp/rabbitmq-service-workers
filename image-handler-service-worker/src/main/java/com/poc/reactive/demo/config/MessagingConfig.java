package com.poc.reactive.demo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class MessagingConfig {

	@Bean
	DirectExchange direct() {
		return new DirectExchange("image-processing-exchange");
	}

	@Profile("resizer")
	static class Resize {
		@Bean
		Queue autoDeleteQueue1() {
			return new Queue("image-processing-queue-tasks-resize", true);
		}

		@Bean
		Binding binding1a(DirectExchange topic, Queue autoDeleteQueue1, Environment env) {

			return BindingBuilder.bind(autoDeleteQueue1).to(topic).with("resize");
		}
	}

	@Profile("watermark")
	static class WaterMark {
		@Bean
		Queue autoDeleteQueue1() {
			return new Queue("image-processing-queue-tasks-watermark", true);
		}

		@Bean
		Binding binding1a(DirectExchange topic, Queue autoDeleteQueue1, Environment env) {
			return BindingBuilder.bind(autoDeleteQueue1).to(topic).with("watermark");
		}
	}

	@Bean
	MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
	
	@Bean
	RetryTemplate retryTemplate() {
		RetryTemplate retryTemplate = new RetryTemplate();

		SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
		retryPolicy.setMaxAttempts(3);

		FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
		backOffPolicy.setBackOffPeriod(2000);

		retryTemplate.setRetryPolicy(retryPolicy);
		retryTemplate.setBackOffPolicy(backOffPolicy);

		return retryTemplate;
	}

}
