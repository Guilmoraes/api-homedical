package br.com.homedical.service.impl;

import br.com.homedical.config.Constants;
import br.com.homedical.domain.Document;
import br.com.homedical.domain.Professional;
import br.com.homedical.domain.User;
import br.com.homedical.domain.enumeration.DocumentStatus;
import br.com.homedical.event.DocumentChangeStatusEmailEvent;
import br.com.homedical.event.DocumentEvent;
import br.com.homedical.repository.DocumentRepository;
import br.com.homedical.repository.DocumentTypeRepository;
import br.com.homedical.repository.ProfessionalRepository;
import br.com.homedical.repository.UserRepository;
import br.com.homedical.security.SecurityUtils;
import br.com.homedical.service.DocumentService;
import br.com.homedical.service.exceptions.BusinessException;
import br.com.homedical.service.exceptions.ErrorConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.com.homedical.service.exceptions.ErrorConstants.ERROR_CHECK_IF_PROFESSIONAL_HAVE_DOCUMENTS_WAITING_FOR_APPROVEMENT_PROFESSIONAL_NOT_FOUND;
import static br.com.homedical.service.exceptions.ErrorConstants.ERROR_CHECK_IF_PROFESSIONAL_HAVE_DOCUMENTS_WAITING_FOR_APPROVEMENT_USER_NOT_FOUND;
import static br.com.homedical.service.exceptions.ErrorConstants.ERROR_GET_PROFESSIONAL_DOCUMENTS_PROFESSIONAL_NOT_FOUND;
import static br.com.homedical.service.exceptions.ErrorConstants.ERROR_GET_PROFESSIONAL_DOCUMENTS_USER_NOT_FOUND;
import static br.com.homedical.service.exceptions.ErrorConstants.ERROR_UPLOAD_DOCUMENT_PROFESSIONAL_NOT_FOUND;
import static br.com.homedical.service.exceptions.ErrorConstants.ERROR_UPLOAD_DOCUMENT_TYPE_NOT_FOUND;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final Logger log = LoggerFactory.getLogger(DocumentServiceImpl.class);

    private final DocumentRepository repository;

    private final ApplicationEventPublisher publisher;

    private final ProfessionalRepository professionalRepository;

    private final UserRepository userRepository;

    private final DocumentTypeRepository documentTypeRepository;

    public DocumentServiceImpl(DocumentRepository repository,
                               ApplicationEventPublisher publisher,
                               ProfessionalRepository professionalRepository,
                               UserRepository userRepository,
                               DocumentTypeRepository documentTypeRepository) {
        this.repository = repository;
        this.publisher = publisher;
        this.professionalRepository = professionalRepository;
        this.userRepository = userRepository;
        this.documentTypeRepository = documentTypeRepository;
    }

    @Override
    public Document save(Document document) {
        log.debug("Request to save Document : {}", document);

        document.setProfessional(professionalRepository.findById(document.getProfessional().getId()).orElseThrow(() -> new BusinessException(ERROR_UPLOAD_DOCUMENT_PROFESSIONAL_NOT_FOUND)));
        document.setType(documentTypeRepository.findById(document.getType().getId()).orElseThrow(() -> new BusinessException(ERROR_UPLOAD_DOCUMENT_TYPE_NOT_FOUND)));

        if (document.getFile() != null) {
            publisher.publishEvent(new DocumentEvent.ProcessDocumentEvent(document, false));
        }

        return repository.save(document);
    }

    @Override
    public Document update(Document document) {
        log.debug("Request to update Document : {}", document);

        if (!repository.exists(document.getId())) {
            throw new BusinessException(Constants.ENTITY_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }

        if (document.getFile() != null) {
            publisher.publishEvent(new DocumentEvent.ProcessDocumentEvent(document, document.getProcessed()));
        }

        return repository.save(document);
    }

    @Override
    public Document updateStatus(Document document) {
        if (!repository.exists(document.getId())) {
            throw new BusinessException(Constants.ENTITY_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }

        Document result = repository.save(document);

        publisher.publishEvent(new DocumentChangeStatusEmailEvent(document.getProfessional().getUser().getId()));

        return result;
    }

    @Override
    public Page<Document> findAll(Pageable pageable) {
        log.debug("Request to get all Documents");
        return repository.findAll(pageable);
    }

    @Override
    public List<Document> findAllStatusWaiting() {
        log.debug("Request to get all documents with status waiting approved");
        return repository.findByStatus(DocumentStatus.WAITING_APPROVEMENT);
    }

    @Override
    public Page<Document> findAllProfessionalDocuments(Pageable pageable, String id) {
        log.debug("Request to get all Documents");
        return repository.findAllByProfessionalId(id, pageable);
    }

    @Override
    public Document findOne(String id) {
        log.debug("Request to get Document : {}", id);
        return repository.findById(id)
            .orElseThrow(BusinessException.of(ErrorConstants.ENTITY_NOT_FOUND, HttpStatus.BAD_REQUEST));
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Document : {}", id);

        if (!repository.exists(id)) {
            throw new BusinessException(Constants.ENTITY_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }

        repository.delete(id);
    }

    @Override
    public List<Document> getProfessionalDocuments() {
        log.debug("Request to get professional Documents");
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).orElseThrow(() -> new BusinessException(ERROR_GET_PROFESSIONAL_DOCUMENTS_USER_NOT_FOUND, HttpStatus.NOT_FOUND));
        Professional professional = professionalRepository.findByUser(user).orElseThrow(() -> new BusinessException(ERROR_GET_PROFESSIONAL_DOCUMENTS_PROFESSIONAL_NOT_FOUND, HttpStatus.NOT_FOUND));
        return repository.findByProfessional(professional);
    }

    @Override
    public Boolean checkIfProfessionalHaveDocumentsWaitingForApprovement() {
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).orElseThrow(() -> new BusinessException(ERROR_CHECK_IF_PROFESSIONAL_HAVE_DOCUMENTS_WAITING_FOR_APPROVEMENT_USER_NOT_FOUND, HttpStatus.NOT_FOUND));
        Professional professional = professionalRepository.findByUser(user).orElseThrow(() -> new BusinessException(ERROR_CHECK_IF_PROFESSIONAL_HAVE_DOCUMENTS_WAITING_FOR_APPROVEMENT_PROFESSIONAL_NOT_FOUND, HttpStatus.NOT_FOUND));
        return !repository.findByProfessionalAndStatus(professional, DocumentStatus.WAITING_APPROVEMENT).isEmpty();
    }

    @Override
    public void saveDocuments(List<Document> list) {

        list.forEach(document -> {
            document.setProfessional(professionalRepository.findById(document.getProfessional().getId()).orElseThrow(() -> new BusinessException(ERROR_UPLOAD_DOCUMENT_PROFESSIONAL_NOT_FOUND)));
            document.setType(documentTypeRepository.findById(document.getType().getId()).orElseThrow(() -> new BusinessException(ERROR_UPLOAD_DOCUMENT_TYPE_NOT_FOUND)));
            if (document.getFile() != null) {
                publisher.publishEvent(new DocumentEvent.ProcessDocumentEvent(repository.save(document), false));
            }

        });
    }
}
