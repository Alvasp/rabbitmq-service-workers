package com.poc.reactive.demo.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ResourceLoader;

import com.poc.reactive.demo.messaging.HandleImageRequestMessage;
import com.poc.reactive.demo.messaging.Resolution;

public class HandlerTests {

	@Test
	void testImageResizerSuccess() {
		FileSystemService fileSystemMock = mock(FileSystemService.class);

		URL resource = ResourceLoader.class.getClassLoader().getResource("watermark.png");

		when(fileSystemMock.getFileFromPath(anyString())).thenReturn(Paths.get(resource.getPath()).toFile());

		ImageHandler handler = new ImageResizerService(fileSystemMock);

		handler.handleImage(new HandleImageRequestMessage("anything", new Resolution(800, 600)));
	}

	@Test
	void testImageResizer_fileNotFound() {
		File f = mock(File.class);
		FileSystemService fileSystemMock = mock(FileSystemService.class);

		when(f.exists()).thenReturn(false);
		when(fileSystemMock.getFileFromPath(anyString())).thenReturn(f);

		assertThrows(RuntimeException.class, () -> {
			ImageHandler handler = new ImageResizerService(fileSystemMock);

			handler.handleImage(new HandleImageRequestMessage("anything", new Resolution(800, 600)));
		});
	}

	@Test
	void testWaterMark_fileNotFound() {
		File f = mock(File.class);
		FileSystemService fileSystemMock = mock(FileSystemService.class);

		when(f.exists()).thenReturn(false);
		when(fileSystemMock.getFileFromPath(anyString())).thenReturn(f);

		assertThrows(RuntimeException.class, () -> {
			ImageHandler handler = new WaterMarkAdderService(fileSystemMock);

			handler.handleImage(new HandleImageRequestMessage("anything", new Resolution(800, 600)));
		});
	}

	@Test
	void testWaterMarkSuccess() {
		FileSystemService fileSystemMock = mock(FileSystemService.class);

		URL resource = ResourceLoader.class.getClassLoader().getResource("watermark.png");

		when(fileSystemMock.getFileFromPath(anyString())).thenReturn(Paths.get(resource.getPath()).toFile());

		ImageHandler handler = new WaterMarkAdderService(fileSystemMock);

		handler.handleImage(new HandleImageRequestMessage("anything", new Resolution(800, 600)));
	}

}
