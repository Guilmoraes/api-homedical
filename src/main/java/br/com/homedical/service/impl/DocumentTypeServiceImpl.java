package br.com.homedical.service.impl;

import br.com.homedical.domain.Document;
import br.com.homedical.domain.DocumentType;
import br.com.homedical.domain.Professional;
import br.com.homedical.domain.User;
import br.com.homedical.domain.enumeration.DocumentStatus;
import br.com.homedical.repository.DocumentRepository;
import br.com.homedical.repository.DocumentTypeRepository;
import br.com.homedical.repository.ProfessionalRepository;
import br.com.homedical.repository.UserRepository;
import br.com.homedical.security.SecurityUtils;
import br.com.homedical.service.DocumentTypeService;
import br.com.homedical.service.exceptions.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static br.com.homedical.service.exceptions.ErrorConstants.ERROR_GET_PENDING_DOCUMENT_TYPES_FOR_PROFESSIONAL_PROFESSIONAL_NOT_FOUND;
import static br.com.homedical.service.exceptions.ErrorConstants.ERROR_GET_PENDING_DOCUMENT_TYPES_FOR_PROFESSIONAL_USER_NOT_FOUND;


@Service
public class DocumentTypeServiceImpl implements DocumentTypeService {

    private final Logger log = LoggerFactory.getLogger(DocumentTypeServiceImpl.class);

    private final DocumentTypeRepository documentTypeRepository;

    private final UserRepository userRepository;

    private final ProfessionalRepository professionalRepository;

    private final DocumentRepository documentRepository;

    public DocumentTypeServiceImpl(DocumentTypeRepository documentTypeRepository,
                                   UserRepository userRepository,
                                   ProfessionalRepository professionalRepository,
                                   DocumentRepository documentRepository) {
        this.documentTypeRepository = documentTypeRepository;
        this.userRepository = userRepository;
        this.professionalRepository = professionalRepository;
        this.documentRepository = documentRepository;
    }


    @Override
    public DocumentType save(DocumentType documentType) {
        log.debug("Request to save DocumentType : {}", documentType);
        return documentTypeRepository.save(documentType);
    }

    @Override
    public DocumentType update(DocumentType documentType) {
        log.debug("Request to update DocumentType : {}", documentType);
        return documentTypeRepository.save(documentType);
    }

    @Override
    public Page<DocumentType> findAll(Pageable pageable) {
        log.debug("Request to get all DocumentTypes");
        return documentTypeRepository.findAll(pageable);
    }


    @Override
    public DocumentType findOne(String id) {
        log.debug("Request to get DocumentType : {}", id);
        return documentTypeRepository.findOne(id);
    }


    @Override
    public void delete(String id) {
        log.debug("Request to delete DocumentType : {}", id);
        documentTypeRepository.delete(id);
    }

    @Override
    public List<DocumentType> getPendingDocumentTypesForProfessional() {
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin())
            .orElseThrow(() -> new BusinessException(ERROR_GET_PENDING_DOCUMENT_TYPES_FOR_PROFESSIONAL_USER_NOT_FOUND));

        Professional professional = professionalRepository.findByUser(user)
            .orElseThrow(() -> new BusinessException(ERROR_GET_PENDING_DOCUMENT_TYPES_FOR_PROFESSIONAL_PROFESSIONAL_NOT_FOUND));

        List<Document> professionalDocuments = documentRepository.findByProfessional(professional);

        List<DocumentType> pendingDocumentTypes = new ArrayList<>();

        List<DocumentType> documentTypes = documentTypeRepository.findByRequiredIsTrue();

        documentTypes.forEach(it -> {
            if (professionalDocuments.stream().anyMatch(document -> document.getType().equals(it) && (document.getStatus().equals(DocumentStatus.APPROVED) || document.getStatus().equals(DocumentStatus.WAITING_APPROVEMENT)))) {
                return;
            }
            pendingDocumentTypes.add(it);
        });

        return pendingDocumentTypes;
    }

}
