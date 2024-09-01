package com.poc.reactive.demo.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public abstract class ConversionUtils {
	private static final String IMAGE_FORMAT = "jpeg";

	public static InputStream bufferedImageToStream(BufferedImage image) throws IOException {
		// Create a ByteArrayOutputStream to hold the image data
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		// Write the image to the ByteArrayOutputStream in the specified format (e.g.,
		// "png", "jpg")
		ImageIO.write(image, IMAGE_FORMAT, byteArrayOutputStream);

		// Convert the ByteArrayOutputStream to a ByteArrayInputStream
		InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

		// Return the InputStream
		return inputStream;
	};

}
