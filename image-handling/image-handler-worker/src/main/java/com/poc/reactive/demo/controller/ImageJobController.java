package com.poc.reactive.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.poc.reactive.demo.dto.AddJobRequest;
import com.poc.reactive.demo.messaging.HandleImageResponseMessage;
import com.poc.reactive.demo.service.JobService;

@RestController(value = "jobs")
public class ImageJobController {

	@Autowired
	JobService service;

	@GetMapping(value="/id/{uuid}")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ResponseEntity<HandleImageResponseMessage> getJob(@PathVariable String uuid) {
		Optional<HandleImageResponseMessage> response = service.getJOBByUUID(uuid);

		if (response.isPresent()) {
			return new ResponseEntity(response.get(), HttpStatusCode.valueOf(200));
		}

		return new ResponseEntity(null, HttpStatusCode.valueOf(404));
	}

	@PostMapping
	public String addJob(@RequestBody AddJobRequest request) {
		return service.saveJOBResponse(request).toString();

	}

}
