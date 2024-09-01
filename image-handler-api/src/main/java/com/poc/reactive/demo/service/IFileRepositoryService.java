package com.poc.reactive.demo.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Interface for file handling operations using some repository
 */
public interface IFileRepositoryService {

	public File get(String name) throws IllegalArgumentException;

	public void save(InputStream inputStream, String name) throws IOException;
}
