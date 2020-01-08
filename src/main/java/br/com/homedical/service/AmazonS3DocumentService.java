package br.com.homedical.service;

import br.com.homedical.domain.DocumentS3;

import java.util.concurrent.CompletableFuture;

public interface AmazonS3DocumentService {

    CompletableFuture<DocumentS3> updateDocument(DocumentS3 documentS3, String fileContentType);

    CompletableFuture<DocumentS3> createDocument(DocumentS3 documentS3, String fileContentType);

    void deleteDocument(DocumentS3 documentS3);

    String generatePresignedUrl(DocumentS3 documentS3);

    CompletableFuture<DocumentS3> processDocument(DocumentS3 documentS3, String fileContentType);
}
