package com.poc.reactive.demo.services.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.poc.reactive.demo.exceptions.ImageHandlingException;
import com.poc.reactive.demo.messaging.dto.Resolution;
import com.poc.reactive.demo.services.ITransformationJob;
import com.poc.reactive.demo.utils.ConversionUtils;

import net.coobird.thumbnailator.Thumbnails;

public class TransformationResize implements ITransformationJob {

	@Override
	public InputStream transform(InputStream sourceFile, Resolution configuration) throws ImageHandlingException {
		System.out.println("imageResizer start");

		try {
			List<BufferedImage> output = Thumbnails.of(sourceFile).size(configuration.width(), configuration.height())
					.asBufferedImages();

			return ConversionUtils.bufferedImageToStream(output.get(0));
		} catch (IOException e) {
			e.printStackTrace();
			throw new ImageHandlingException("unable to add watermark to image: " + e.getMessage(), e);
		}
	}

}
