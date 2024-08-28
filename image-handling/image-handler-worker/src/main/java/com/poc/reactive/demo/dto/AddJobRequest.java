package com.poc.reactive.demo.dto;

import com.poc.reactive.demo.messaging.Resolution;

public record AddJobRequest(JobTypes type, String filename, Resolution resolution) {

}
