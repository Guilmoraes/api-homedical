package br.com.homedical.service;

import br.com.homedical.domain.DocumentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DocumentTypeService {

    DocumentType save(DocumentType documentType);

    DocumentType update(DocumentType documentType);

    Page<DocumentType> findAll(Pageable pageable);

    DocumentType findOne(String id);

    void delete(String id);

    List<DocumentType> getPendingDocumentTypesForProfessional();

}
