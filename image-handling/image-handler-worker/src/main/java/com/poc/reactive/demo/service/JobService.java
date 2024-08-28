package com.poc.reactive.demo.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poc.reactive.demo.dto.AddJobRequest;
import com.poc.reactive.demo.messaging.HandleImageRequestMessage;
import com.poc.reactive.demo.messaging.HandleImageResponseMessage;
import com.poc.reactive.demo.messaging.Resolution;
import com.poc.reactive.demo.repository.JobRepository;

@Service
public class JobService {

	@Autowired
	JobRepository repo;

	@Autowired
	RabbitTemplate template;

	public Optional<HandleImageResponseMessage> getJOBByUUID(String uuid) {
		return this.repo.getJOBByUUID(uuid);
	}

	public UUID saveJOBResponse(AddJobRequest addJob) {
		UUID identifier = UUID.randomUUID();

		System.out.println("adding job id: " + identifier);

		HandleImageRequestMessage message = new HandleImageRequestMessage(addJob.filename(),
				new Resolution(addJob.resolution().width(), addJob.resolution().height()));

		template.convertAndSend("image-processing-exchange", addJob.type().getDescription(), message,
				messagePostProcessor -> {
					messagePostProcessor.getMessageProperties().setReplyTo("image-processing-queue-tasks-response");
					messagePostProcessor.getMessageProperties().setCorrelationId(identifier.toString());
					return messagePostProcessor;
				});

		// storing an empty response
		this.repo.saveJOBResponse(identifier.toString(), null);

		return identifier;
	}

	public void updateJOBResponse(String identifier, HandleImageResponseMessage response) {
		System.out.println("updating job id: " + identifier);
		this.repo.saveJOBResponse(identifier, response);
	}
}
