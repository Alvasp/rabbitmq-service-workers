package com.poc.reactive.demo.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.poc.reactive.demo.messaging.dto.ImageJob;

@Repository
public class JobRepository {

	private Map<String, ImageJob> db = new HashMap<String, ImageJob>();

	public Optional<ImageJob> getJob(String uuid) {
		return Optional.ofNullable(this.db.get(uuid));
	}

	public void saveJob(ImageJob response) {
		this.db.put(response.uuid(), response);
	}

}
