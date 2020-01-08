package br.com.homedical.service;

import br.com.homedical.event.ProcessPhotoEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.event.TransactionalEventListener;

public interface PhotoUploadService extends BaseUploadService {

    @Scheduled(cron = "0 1 * * * *")
    void uploadPhotos();

    @Scheduled(cron = "0 3 * * * *")
    void unprocessed();

    @Async
    @TransactionalEventListener(fallbackExecution = true)
    void processPhotoEvent(ProcessPhotoEvent event);

}
