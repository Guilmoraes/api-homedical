package br.com.homedical.service.impl;

import br.com.homedical.domain.DocumentS3;
import br.com.homedical.domain.File;
import br.com.homedical.event.FileEvent;
import br.com.homedical.repository.FileRepository;
import br.com.homedical.service.AmazonS3DocumentService;
import br.com.homedical.service.FileUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    private final Logger log = LoggerFactory.getLogger(FileUploadServiceImpl.class);

    private final FileRepository fileRepository;

    private final AmazonS3DocumentService amazonS3DocumentService;

    public FileUploadServiceImpl(FileRepository fileRepository,
                                 AmazonS3DocumentService amazonS3DocumentService) {
        this.fileRepository = fileRepository;
        this.amazonS3DocumentService = amazonS3DocumentService;
    }

    @Scheduled(cron = "0 1 * * * *")
    @Transactional
    @Override
    public void uploadFiles() {
        log.info("Start upload file");
        fileRepository
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

    @Override
    public void uploadFilesByIds(List<String> ids) {
        fileRepository
            .findByIdIn(ids)
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

    @Override
    @Async
    @TransactionalEventListener(fallbackExecution = true)
    public void processFilesEvent(FileEvent.StartFileUploadProcess event) {
        this.uploadFiles();
    }

    @Override
    @Async
    @TransactionalEventListener(fallbackExecution = true)
    public void startFilesUploadByIds(FileEvent.StartFilesUploadByIds event) {
        this.uploadFilesByIds(event.getIds());
    }

    private void update(CompletableFuture<DocumentS3> document) {
        document.whenComplete((documentS3, throwable) -> {
            if (documentS3 != null) {
                File p = (File) documentS3;
                p.setFile(null);
                p.setProcessed(true);
                fileRepository.save(p);
            }
        });
    }
}
