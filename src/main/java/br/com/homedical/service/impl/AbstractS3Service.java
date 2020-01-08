package br.com.homedical.service.impl;

import br.com.homedical.config.ApplicationProperties;
import br.com.homedical.service.exceptions.BusinessException;
import br.com.homedical.service.exceptions.ErrorConstants;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.scheduling.annotation.Async;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

/**
 * Created by batman
 */
public abstract class AbstractS3Service {

    protected static final String S3_PREFIX = "s3://";
    protected final ResourceLoader provider;
    protected final AmazonS3Client amazonS3Client;
    protected final ApplicationProperties properties;
    protected final String bucket;
    private final Logger log = LoggerFactory.getLogger(AbstractS3Service.class);

    protected AbstractS3Service(ResourceLoader provider, AmazonS3Client amazonS3Client, ApplicationProperties properties, String bucket) {
        this.provider = provider;
        this.amazonS3Client = amazonS3Client;
        this.properties = properties;
        this.bucket = bucket;
    }

    protected String getUrl(String name) {
        Optional<URL> url = Optional.ofNullable(this.amazonS3Client.getUrl(bucket, name));
        return url.map(URL::toString)
            .orElseThrow(() -> new BusinessException(ErrorConstants.S3_URL_NOT_AVAILABLE));
    }

    public void upload(String name, File file, String fileContentType) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, name, file);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(fileContentType);
        putObjectRequest.setMetadata(metadata);
        amazonS3Client.putObject(putObjectRequest);
    }

    @Async
    public void upload(String name, byte[] file) {
        Resource resource = this.provider.getResource(S3_PREFIX + bucket + "/" + name);

        WritableResource writableResource = (WritableResource) resource;
        try (OutputStream outputStream = writableResource.getOutputStream()) {
            outputStream.write(file);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Error uploading file to s3", e);
        }
    }

    @Async
    public void delete(String key) {
        amazonS3Client.deleteObject(properties.getAws().getBucketDocuments(), key);
    }

    protected URL generatePreSignedUrl(String key) {
        URL url = amazonS3Client.generatePresignedUrl(this.bucket, key,
            Date.from(Instant.now().plusMillis(properties.getAws().getUrlExpiration())));
        return Optional
            .ofNullable(url)
            .orElseThrow(() -> new BusinessException(ErrorConstants.S3_URL_NOT_AVAILABLE));
    }

}
