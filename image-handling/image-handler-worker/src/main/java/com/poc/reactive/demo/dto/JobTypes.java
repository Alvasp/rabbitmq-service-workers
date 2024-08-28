package com.poc.reactive.demo.dto;

public enum JobTypes {
	
	RESIZE("resize"),
	WATERMARK("watermark");
	
	private final String description;

    // Constructor
	JobTypes(String description) {
        this.description = description;
    }

    // Getter for the description
    public String getDescription() {
        return description;
    }
	
}
