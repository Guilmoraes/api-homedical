package br.com.homedical.service.impl;

import br.com.homedical.config.ApplicationProperties;
import br.com.homedical.domain.DocumentS3;
import br.com.homedical.service.AmazonS3DocumentService;
import com.amazonaws.services.s3.AmazonS3Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("Duplicates")
@Service
public class AmazonS3DocumentServiceImpl extends AbstractS3Service implements AmazonS3DocumentService {

    private final Logger log = LoggerFactory.getLogger(AmazonS3DocumentServiceImpl.class);

    public AmazonS3DocumentServiceImpl(ResourceLoader provider,
                                       AmazonS3Client amazonS3Client,
                                       ApplicationProperties properties) {
        super(provider, amazonS3Client, properties, properties.getAws().getBucketDocuments());
    }

    @Async
    public CompletableFuture<DocumentS3> updateDocument(DocumentS3 documentS3, String fileContentType) {
        return processDocument(documentS3, fileContentType);
    }

    @Async
    public CompletableFuture<DocumentS3> createDocument(DocumentS3 documentS3, String fileContentType) {
        return processDocument(documentS3, fileContentType);
    }

    @Override
    public void deleteDocument(DocumentS3 documentS3) {
        if (documentS3.getS3Name() == null || documentS3.getS3Name().isEmpty()) {
            throw new IllegalArgumentException("Name is null");
        }

        delete(documentS3.getS3Name());
    }

    @Override
    public String generatePresignedUrl(DocumentS3 documentS3) {
        if (documentS3.getS3Name() == null || documentS3.getS3Name().isEmpty()) {
            throw new IllegalArgumentException("Name is null");
        }

        return generatePreSignedUrl(documentS3.getS3Name()).toString();
    }

    @Override
    public CompletableFuture<DocumentS3> processDocument(DocumentS3 documentS3, String fileContentType) {

        log.info("Processing photos from async service...");

        if (documentS3.getS3Name() == null || documentS3.getS3Name().isEmpty()) {
            throw new IllegalArgumentException("S3 Name is null");
        }

        if (documentS3.getFile() == null) {
            return CompletableFuture.completedFuture(null);
        }

        log.info("Uploading to s3...");

        documentS3.setUrl(getUrl(documentS3.getS3Name()));

        File fileToSend = new File(documentS3.getFileName());
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(fileToSend);
            fileOutputStream.write(documentS3.getFile());
        } catch (java.io.IOException e) {
            log.error("Erro converting file: {}", e);
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                log.error("Error in closing fos.");
            }
        }

        upload(documentS3.getS3Name(), fileToSend, fileContentType);

        return CompletableFuture.completedFuture(documentS3);
    }

}
