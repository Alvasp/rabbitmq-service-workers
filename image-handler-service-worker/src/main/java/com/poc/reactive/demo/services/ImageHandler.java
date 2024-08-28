package com.poc.reactive.demo.services;

import com.poc.reactive.demo.exceptions.ImageHandlingException;
import com.poc.reactive.demo.messaging.HandleImageRequestMessage;

public abstract class ImageHandler {

	protected String FIXED_STORAGE_PATH = "/Users/alvasp/Downloads/";
	
	protected FileSystemService fileSystemService;
	
	public abstract void handleImage(HandleImageRequestMessage mesage) throws ImageHandlingException;
	
	public ImageHandler(FileSystemService fileSystem) {
		this.fileSystemService = fileSystem;
	}
	
}
