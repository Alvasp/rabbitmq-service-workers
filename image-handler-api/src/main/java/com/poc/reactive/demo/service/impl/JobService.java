package com.poc.reactive.demo.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poc.reactive.demo.contract.AddJobRequest;
import com.poc.reactive.demo.exceptions.FileHandlingException;
import com.poc.reactive.demo.messaging.dto.ImageHandleInput;
import com.poc.reactive.demo.messaging.dto.ImageHandleOutput;
import com.poc.reactive.demo.messaging.dto.ImageJob;
import com.poc.reactive.demo.messaging.dto.Resolution;
import com.poc.reactive.demo.repository.JobRepository;
import com.poc.reactive.demo.service.IFileRepositoryService;
import com.poc.reactive.demo.service.IJobService;

@Service
public class JobService implements IJobService {

	@Autowired
	JobRepository jobRepository;

	@Autowired
	IFileRepositoryService filesRepository;

	@Autowired
	RabbitTemplate messageTemplate;

	Logger logger = LoggerFactory.getLogger(JobService.class);

	@Override
	public List<ImageJob> get(List<String> uuid) {
		return uuid.stream().map(ui -> {
			Optional<ImageJob> result = this.jobRepository.getJob(ui);

			return result.orElse(new ImageJob(ui, false, new ImageHandleOutput(false, null, "not found")));
		}).collect(Collectors.toList());
	}

	@Override
	public UUID save(AddJobRequest addJob) {
		UUID identifier = UUID.randomUUID();

		logger.info("adding job id: " + identifier);

		try (InputStream stream = addJob.file().getInputStream()) {
			filesRepository.save(stream, addJob.filename());
		} catch (IOException e) {
			logger.error("errir handling file upload", e);
			throw new FileHandlingException("unable to upload file", e);
		}

		logger.info("saving job");

		this.jobRepository.saveJob(new ImageJob(identifier.toString(), true, null));

		logger.info("dispatching async job for id: " + identifier);

		ImageHandleInput message = new ImageHandleInput(addJob.filename(),
				new Resolution(addJob.resolution().width(), addJob.resolution().height()));

		messageTemplate.convertAndSend("image-processing-exchange", addJob.type().getDescription(), message,
				messagePostProcessor -> {
					messagePostProcessor.getMessageProperties().setReplyTo("image-processing-queue-tasks-response");
					messagePostProcessor.getMessageProperties().setCorrelationId(identifier.toString());
					return messagePostProcessor;
				});

		return identifier;
	}

	@Override
	public void update(String uuid, ImageHandleOutput response) {
		logger.info("updating job id: " + uuid);
		Optional<ImageJob> result = this.jobRepository.getJob(uuid);

		if (result.isEmpty()) {
			throw new RuntimeException("not found entity");
		}

		this.jobRepository.saveJob(new ImageJob(uuid, false, response));
	}

	@Override
	public byte[] getFile(String filename) {
		File f = filesRepository.get(filename);

		byte[] byteArray = new byte[(int) f.length()];

		try (FileInputStream inputStream = new FileInputStream(f)) {
			inputStream.read(byteArray);
			return byteArray;
		} catch (IOException e) {
			throw new RuntimeException("file not found");
		}
	}

}
