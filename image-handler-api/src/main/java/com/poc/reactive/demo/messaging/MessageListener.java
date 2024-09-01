package com.poc.reactive.demo.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.reactive.demo.messaging.dto.ImageHandleOutput;
import com.poc.reactive.demo.service.impl.JobService;

@Component
public class MessageListener {

	@Autowired
	private RetryTemplate retryTemplate;

	@Autowired
	private JobService service;

	@Autowired
	ObjectMapper mapper;

	Logger logger = LoggerFactory.getLogger(MessageListener.class);

	@RabbitListener(queues = "#{autoDeleteQueue1.name}")
	public void receive1(Message message) throws Exception {
		retryTemplate.execute(new RetryCallback<Void, Exception>() {
			@Override
			public Void doWithRetry(RetryContext context) throws Exception {
				logger.info("message received in web: " + message.toString());
				
				String correlationId = new String(message.getMessageProperties().getCorrelationId());
				String body = new String(message.getBody());

				ImageHandleOutput reponse = mapper.readValue(body, ImageHandleOutput.class);
				service.update(correlationId, reponse);
				return null;
			}

		}, new RecoveryCallback<Void>() {
			@Override
			public Void recover(RetryContext context) throws Exception {
				logger.error("Max retries reached, handling recovery...", context.getLastThrowable());

				return null;
			}
		});
	}

}