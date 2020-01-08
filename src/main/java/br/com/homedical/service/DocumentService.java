package br.com.homedical.service;

import br.com.homedical.domain.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DocumentService {

    Document save(Document document);

    Document update(Document document);

    Page<Document> findAll(Pageable pageable);

    Page<Document> findAllProfessionalDocuments(Pageable pageable, String id);

    List<Document> findAllStatusWaiting();

    Document findOne(String id);

    Document updateStatus(Document document);

    void delete(String id);

    List<Document> getProfessionalDocuments();

    Boolean checkIfProfessionalHaveDocumentsWaitingForApprovement();

    void saveDocuments(List<Document> list);
}
