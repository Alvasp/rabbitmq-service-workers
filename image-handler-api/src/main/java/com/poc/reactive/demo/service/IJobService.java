package com.poc.reactive.demo.service;

import java.util.List;
import java.util.UUID;

import com.poc.reactive.demo.contract.AddJobRequest;
import com.poc.reactive.demo.exceptions.FileHandlingException;
import com.poc.reactive.demo.messaging.dto.ImageHandleOutput;
import com.poc.reactive.demo.messaging.dto.ImageJob;

public interface IJobService {

	public List<ImageJob> get(List<String> uuid);

	public UUID save(AddJobRequest addJob) throws FileHandlingException;

	public void update(String uuid, ImageHandleOutput response);
	
	public byte[] getFile(String filename) throws FileHandlingException;

}
