package com.poc.reactive.demo.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.reactive.demo.messaging.dto.ImageHandleInput;
import com.poc.reactive.demo.messaging.dto.ImageHandleOutput;
import com.poc.reactive.demo.services.ITransformationHandler;

@Component
public class MessageListener {

	@Autowired
	private RetryTemplate retryTemplate;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private ITransformationHandler handler;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	Logger logger = LoggerFactory.getLogger(MessageListener.class);

	@RabbitListener(queues = "#{autoDeleteQueue1.name}")
	public void receive1(Message message) throws Exception {

		retryTemplate.execute(new RetryCallback<Void, Exception>() {
			@Override
			public Void doWithRetry(RetryContext context) throws Exception {
				logger.info("message received, delegating to handler:  " + message.toString());

				ImageHandleInput body = mapper.readValue(new String(message.getBody()), ImageHandleInput.class);

				ImageHandleOutput output = handler.handleImageTransformation(body);

				reply(message, output);

				return null;
			}

		}, new RecoveryCallback<Void>() {
			@Override
			public Void recover(RetryContext context) throws Exception {
				logger.error("Max retries reached, handling recovery...", context.getLastThrowable());
				reply(message, new ImageHandleOutput(false, null, context.getLastThrowable().getMessage()));
				return null;
			}
		});
	}

	private void reply(Message message, ImageHandleOutput response) {
		String correlationId = new String(message.getMessageProperties().getCorrelationId());
		String replyToQueue = message.getMessageProperties().getReplyTo();

		logger.info("sending reply to " + correlationId + " " + replyToQueue);

		rabbitTemplate.convertAndSend("", replyToQueue, response, messagePostProcessor -> {
			messagePostProcessor.getMessageProperties().setReplyTo(replyToQueue);
			messagePostProcessor.getMessageProperties().setCorrelationId(correlationId);
			return messagePostProcessor;
		});

	}

}