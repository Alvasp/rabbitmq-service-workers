package com.poc.reactive.demo.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.poc.reactive.demo.service.IFileRepositoryService;

@Service
public class FileSystemRepositoryService implements IFileRepositoryService {
	@Value("${app.localrepository}")
	private String PATH;

	@Override
	public File get(String name) throws IllegalArgumentException {
		File file = new File(PATH + name);

		if (!file.exists()) {
			throw new IllegalArgumentException("File not found");
		}

		return file;
	}

	@Override
	public void save(InputStream inputStream, String name) throws IOException {
		Path path = Paths.get(PATH + name);
		Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
		inputStream.close();
	}
}
