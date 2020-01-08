package br.com.homedical.service;

import br.com.homedical.domain.Document;
import br.com.homedical.event.DocumentEvent;
import br.com.homedical.event.FileEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.event.TransactionalEventListener;

@SuppressWarnings("unused")
public interface DocumentUploadService extends BaseUploadService {

    void uploadDocuments();

    void unprocessed();

    void uploadDocumentsAsync();

    @Async
    @TransactionalEventListener(fallbackExecution = true)
    void processDocumentEvent(DocumentEvent.ProcessDocumentEvent event);

    void deleteDocument(Document document);


}
