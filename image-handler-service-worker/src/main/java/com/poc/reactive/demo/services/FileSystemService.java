package com.poc.reactive.demo.services;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

@Service
public class FileSystemService {

	public File getFileFromPath(String path) throws IllegalArgumentException {
		File file = new File(path);

		if (!file.exists()) {
			throw new IllegalArgumentException("File not found");
		}

		return file;
	}

	public void saveToPath(BufferedImage image, String path) throws IllegalArgumentException {
		File outputFile = new File(path);
		List<String> extension = Arrays.asList(path.split("\\."));
		
		try {
			ImageIO.write(image, extension.get(extension.size() - 1), outputFile);
		} catch (IOException e) {
			throw new IllegalArgumentException("error writing file");
		}
	}
}
