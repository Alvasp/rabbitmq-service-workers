package com.poc.reactive.demo.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.poc.reactive.demo.messaging.HandleImageResponseMessage;

@Repository
public class JobRepository {

	private Map<String, HandleImageResponseMessage> db = new HashMap<String, HandleImageResponseMessage>();

	public Optional<HandleImageResponseMessage> getJOBByUUID(String uuid) {
		return Optional.ofNullable(this.db.get(uuid));
	}

	public void saveJOBResponse(String uuid, HandleImageResponseMessage response) {
		this.db.put(uuid, response);
	}

}
