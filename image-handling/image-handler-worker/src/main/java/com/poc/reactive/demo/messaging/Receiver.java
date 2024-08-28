package com.poc.reactive.demo.messaging;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.reactive.demo.service.JobService;

@Component
public class Receiver {
	@Autowired
	private RetryTemplate retryTemplate;

	@Autowired
	private JobService service;

	@Autowired
	ObjectMapper mapper;

	@RabbitListener(queues = "#{autoDeleteQueue1.name}")
	public void receive1(Message message) throws Exception {
		String correlationId = new String(message.getMessageProperties().getCorrelationId());

		retryTemplate.execute(new RetryCallback<Void, Exception>() {
			@Override
			public Void doWithRetry(RetryContext context) throws Exception {
				System.out.println("message received in web: " + message.toString());
				String body = new String(message.getBody());

				HandleImageResponseMessage reponse = mapper.readValue(body, HandleImageResponseMessage.class);
				service.updateJOBResponse(correlationId, reponse);
				return null;
			}

		}, new RecoveryCallback<Void>() {
			@Override
			public Void recover(RetryContext context) throws Exception {
				System.err.println("Max retries reached, handling recovery...");

				System.out.println(context.getLastThrowable());
				return null;
			}
		});
	}

}