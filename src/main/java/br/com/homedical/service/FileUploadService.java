package br.com.homedical.service;

import br.com.homedical.event.FileEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@SuppressWarnings("unused")
public interface FileUploadService extends BaseUploadService {

    void uploadFiles();

    void uploadFilesByIds(List<String> ids);

    @Async
    @TransactionalEventListener(fallbackExecution = true)
    void processFilesEvent(FileEvent.StartFileUploadProcess event);

    @Async
    @TransactionalEventListener(fallbackExecution = true)
    void startFilesUploadByIds(FileEvent.StartFilesUploadByIds event);

}
