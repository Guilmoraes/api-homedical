package br.com.homedical.service.impl;

import br.com.homedical.config.ApplicationProperties;
import br.com.homedical.domain.ImageS3;
import br.com.homedical.service.AmazonS3ImageService;
import com.amazonaws.services.s3.AmazonS3Client;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class AmazonS3ImageServiceImpl implements AmazonS3ImageService {

    private final Logger log = LoggerFactory.getLogger(AmazonS3ImageServiceImpl.class);

    private static final String S3_PREFIX = "s3://";

    private final ResourceLoader provider;

    private final AmazonS3Client amazonS3Client;

    private final ApplicationProperties properties;

    public AmazonS3ImageServiceImpl(ResourceLoader provider, AmazonS3Client amazonS3Client, ApplicationProperties properties) {
        this.provider = provider;
        this.amazonS3Client = amazonS3Client;
        this.properties = properties;
    }

    public CompletableFuture<ImageS3> updateImage(ImageS3 imageS3) {
        return processImage(imageS3);
    }

    public CompletableFuture<ImageS3> createImage(ImageS3 imageS3) {
        return processImage(imageS3);
    }

    public void deleteImage(ImageS3 imageS3) {
        if (imageS3.getOriginalName() == null || imageS3.getOriginalName().isEmpty()) {
            throw new IllegalArgumentException("Original Name is null");
        }

        if (imageS3.getMediumName() == null || imageS3.getMediumName().isEmpty()) {
            throw new IllegalArgumentException("Medium Name is null");
        }

        if (imageS3.getThumbnailName() == null || imageS3.getThumbnailName().isEmpty()) {
            throw new IllegalArgumentException("Thumbnail Name is null");
        }

        delete(imageS3.getOriginalName());
        delete(imageS3.getMediumName());
        delete(imageS3.getThumbnailName());
    }

    @Async
    public CompletableFuture<ImageS3> processImage(ImageS3 imageS3) {

        if (imageS3.getOriginalName() == null || imageS3.getOriginalName().isEmpty()) {
            throw new IllegalArgumentException("Original Name is null");
        }

        if (imageS3.getMediumName() == null || imageS3.getMediumName().isEmpty()) {
            throw new IllegalArgumentException("Medium Name is null");
        }

        if (imageS3.getThumbnailName() == null || imageS3.getThumbnailName().isEmpty()) {
            throw new IllegalArgumentException("Thumbnail Name is null");
        }

        log.info("Processing photos from async service...");
        if (imageS3.getFile() == null) {
            return CompletableFuture.completedFuture(null);
        }

        BufferedImage image = bufferImage(imageS3.getFile());

        log.info("Uploading to s3...");

        File original = renderFile(image, 0);
        imageS3.setOriginal(getUrl(imageS3.getOriginalName()));
        upload(imageS3.getOriginalName(), original);

        File medium = renderFile(image, 400);
        imageS3.setMedium(getUrl(imageS3.getMediumName()));
        upload(imageS3.getMediumName(), medium);

        File thumbnail = renderFile(image, 200);
        imageS3.setThumbnail(getUrl(imageS3.getThumbnailName()));
        upload(imageS3.getThumbnailName(), thumbnail);

        return CompletableFuture.completedFuture(imageS3);
    }

    private BufferedImage bufferImage(byte[] file) {
        log.info("Reading image...");
        ByteArrayInputStream stream = new ByteArrayInputStream(file);
        BufferedImage image = null;

        try {
            image = ImageIO.read(stream);
        } catch (IOException e) {
            log.error("Error reading image", e);
        }

        if (image == null) {
            throw new IllegalArgumentException("Image is null for uploading");
        }

        return image;
    }

    private File renderFile(BufferedImage image, Integer size) {
        File destFile = null;
        try {
            destFile = File.createTempFile("homedical" + UUID.randomUUID().toString(), ".png");

            if (size > 0) {
                BufferedImage resize150 = Scalr.resize(image, 200);
                ImageIO.write(resize150, "png", destFile);
            } else {
                ImageIO.write(image, "png", destFile);
            }

        } catch (IOException ase) {
            log.error("Error uploading to S3", ase);
        }

        if (destFile == null) {
            throw new IllegalArgumentException("Image is null for uploading");
        }

        return destFile;
    }

    private String getUrl(String name) {
        return this.amazonS3Client.getUrl(properties.getAws().getBucketImages(), name).toString();
    }

    public void upload(String name, File file) {
        try {
            byte[] bFile = Files.readAllBytes(file.toPath());
            upload(name, bFile);
        } catch (IOException e) {
            log.error("Error converting file", e);
        }
    }

    public String generatePresignedUrl(ImageS3 imageS3) {
        if (imageS3.getMediumName() == null || imageS3.getMediumName().isEmpty()) {
            throw new IllegalArgumentException("Name is null");
        }

        return generatePreSignedUrl(imageS3.getMediumName()).toString();
    }

    private URL generatePreSignedUrl(String key) {
        return amazonS3Client.generatePresignedUrl(properties.getAws().getBucketDocuments(),
            key,
            Date.from(Instant.now().plusMillis(properties.getAws().getUrlExpiration())));
    }

    @Async
    public void upload(String name, byte[] file) {
        Resource resource = this.provider.getResource(S3_PREFIX + properties.getAws().getBucketImages() + "/" + name);

        WritableResource writableResource = (WritableResource) resource;
        try {
            try (OutputStream outputStream = writableResource.getOutputStream()) {
                outputStream.write(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Async
    public void delete(String key) {
        amazonS3Client.deleteObject(properties.getAws().getBucketImages(), key);
    }

}

