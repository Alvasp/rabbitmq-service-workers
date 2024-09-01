package com.poc.reactive.demo.services.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.core.io.ClassPathResource;

import com.poc.reactive.demo.exceptions.ImageHandlingException;
import com.poc.reactive.demo.messaging.dto.Resolution;
import com.poc.reactive.demo.services.ITransformationJob;
import com.poc.reactive.demo.utils.ConversionUtils;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

public class TransformationWaterMark implements ITransformationJob {

	@Override
	public InputStream transform(InputStream sourceFile, Resolution configuration) throws RuntimeException {
		try {
			List<BufferedImage> output = Thumbnails.of(sourceFile)
					.watermark(Positions.BOTTOM_RIGHT,
							ImageIO.read(new ClassPathResource("watermark.png").getInputStream()), 0.5f)
					.size(configuration.width(), configuration.height()).asBufferedImages();

			return ConversionUtils.bufferedImageToStream(output.get(0));
		} catch (IOException e) {
			e.printStackTrace();
			throw new ImageHandlingException("unable to add watermark to image: " + e.getMessage(), e);
		}
	}

}
