package com.poc.reactive.demo.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poc.reactive.demo.exceptions.ImageHandlingException;
import com.poc.reactive.demo.messaging.dto.ImageHandleInput;
import com.poc.reactive.demo.messaging.dto.ImageHandleOutput;
import com.poc.reactive.demo.services.IFileRepositoryService;
import com.poc.reactive.demo.services.ITransformationJob;
import com.poc.reactive.demo.services.ITransformationHandler;

@Service
public class TransformationHandlerImpl implements ITransformationHandler {

	@Autowired
	private IFileRepositoryService fileRepository;

	@Autowired
	private ITransformationJob job; // this job is injected based on app profile

	@Override
	public ImageHandleOutput handleImageTransformation(ImageHandleInput input) throws ImageHandlingException {

		File file = this.fileRepository.get(input.filename());

		try (InputStream inputStream = new FileInputStream(file)) {

			Thread.sleep(8000); // simulates some expensive operation

			try (InputStream transformedStream = job.transform(inputStream, input.resolution())) {
				String newFilename = "transformed_" + input.filename();
				this.fileRepository.save(transformedStream, newFilename);
				return new ImageHandleOutput(true, newFilename, null);
			}

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new ImageHandlingException("Thread was interrupted during transformation", e);
		} catch (IOException e) {
			throw new ImageHandlingException("Error handling transformation for input file " + input.filename(), e);
		}
	}

}
