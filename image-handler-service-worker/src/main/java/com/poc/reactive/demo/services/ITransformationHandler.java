package com.poc.reactive.demo.services;

import com.poc.reactive.demo.exceptions.ImageHandlingException;
import com.poc.reactive.demo.messaging.dto.ImageHandleInput;
import com.poc.reactive.demo.messaging.dto.ImageHandleOutput;

/**
 * Handles Image transformation process by getting specified file from
 * repository and then delegating the processing with specific job
 */
public interface ITransformationHandler {

	abstract ImageHandleOutput handleImageTransformation(ImageHandleInput input) throws ImageHandlingException;

}
