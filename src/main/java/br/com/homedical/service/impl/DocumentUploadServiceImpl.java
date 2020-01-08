package br.com.homedical.service.impl;

import br.com.homedical.domain.Document;
import br.com.homedical.domain.DocumentS3;
import br.com.homedical.event.DocumentEvent;
import br.com.homedical.repository.DocumentRepository;
import br.com.homedical.service.AmazonS3DocumentService;
import br.com.homedical.service.DocumentUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.concurrent.CompletableFuture;

@Service
public class DocumentUploadServiceImpl implements DocumentUploadService {

    private final Logger log = LoggerFactory.getLogger(DocumentUploadServiceImpl.class);

    private final DocumentRepository documentRepository;

    private final AmazonS3DocumentService amazonS3DocumentService;

    public DocumentUploadServiceImpl(DocumentRepository repository,
                                     AmazonS3DocumentService amazonS3DocumentService) {
        this.documentRepository = repository;
        this.amazonS3DocumentService = amazonS3DocumentService;
    }

    @Scheduled(cron = "0 1 * * * *")
    @Transactional
    @Override
    public void uploadDocuments() {
        log.info("Start upload documents");
        documentRepository
            .findByFileIsNotNull()
            .forEach(it -> {
                if (it.getS3Name() == null || it.getS3Name().isEmpty()) {
                    update(
                        amazonS3DocumentService.createDocument(generateName(it), it.getFileContentType())
                    );
                } else {
                    update(
                        amazonS3DocumentService.updateDocument(it, it.getFileContentType())
                    );
                }
            });
    }

    @Scheduled(cron = "0 3 * * * *")
    @Transactional
    @Override
    public void unprocessed() {
        log.info("Start upload documents");
        documentRepository
            .findByProcessedFalse()
            .forEach(it -> {
                if (it.getS3Name() == null || it.getS3Name().isEmpty()) {
                    amazonS3DocumentService.createDocument(generateName(it), it.getFileContentType());
                } else {
                    amazonS3DocumentService.updateDocument(it, it.getFileContentType());
                }
            });
    }

    @Override
    @Async
    public void uploadDocumentsAsync() {
        log.info("Start upload photos");
        documentRepository
            .findByFileIsNotNull()
            .forEach(it -> {
                if (it.getS3Name() == null || it.getS3Name().isEmpty()) {
                    amazonS3DocumentService.createDocument(generateName(it), it.getFileContentType());
                } else {
                    amazonS3DocumentService.updateDocument(it, it.getFileContentType());
                }
            });
    }

    @Override
    @Async
    @TransactionalEventListener(fallbackExecution = true)
    public void processDocumentEvent(DocumentEvent.ProcessDocumentEvent event) {
        log.info("Start upload photos");
        Document document = event.get();
        if (event.isUpdate()) {
            update(
                amazonS3DocumentService.updateDocument(document, document.getFileContentType())
            );
        } else {
            update(
                amazonS3DocumentService.createDocument(generateName(document), document.getFileContentType())
            );
        }
    }

    private void update(CompletableFuture<DocumentS3> document) {
        document.whenComplete((documentS3, throwable) -> {
            if (documentS3 != null) {
                Document p = (Document) documentS3;
                p.setFile(null);
                p.setProcessed(true);
                documentRepository.save(p);
            }
        });
    }

    @Async
    @Override
    public void deleteDocument(Document document) {
        amazonS3DocumentService.deleteDocument(document);
    }

}
