package com.poc.reactive.demo.services;

import java.io.InputStream;

import com.poc.reactive.demo.exceptions.ImageHandlingException;
import com.poc.reactive.demo.messaging.dto.Resolution;

/**
 * Handles Image transformation job for the specific configuration
 */
public interface ITransformationJob {

	public InputStream transform(InputStream source, Resolution configuration) throws ImageHandlingException;

}
