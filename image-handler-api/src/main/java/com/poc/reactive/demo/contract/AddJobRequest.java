package com.poc.reactive.demo.contract;

import org.springframework.web.multipart.MultipartFile;

import com.poc.reactive.demo.messaging.dto.Resolution;

public record AddJobRequest(MultipartFile file, JobTypes type, String filename, Resolution resolution) {

}
