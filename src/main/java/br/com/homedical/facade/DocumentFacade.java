package br.com.homedical.facade;

import br.com.homedical.domain.Document;
import br.com.homedical.facade.dto.document.DocumentDTO;
import br.com.homedical.facade.dto.document.DocumentUploadDTO;
import br.com.homedical.facade.mapper.DocumentMapper;
import br.com.homedical.service.DocumentService;
import br.com.homedical.web.rest.vm.DocumentsVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentFacade {

    private final Logger log = LoggerFactory.getLogger(DocumentFacade.class);

    private final DocumentService service;

    private final DocumentMapper mapper;

    public DocumentFacade(DocumentService service, DocumentMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Transactional
    public DocumentDTO save(DocumentUploadDTO dto) {
        log.debug("Request to save Document : {}", dto);
        Document document = mapper.fromDocumentUploadDtoToEntity(dto);
        document = service.save(document);
        return mapper.toDto(document);
    }

    @Transactional
    public DocumentDTO update(DocumentDTO dto) {
        log.debug("Request to update Document : {}", dto);
        Document document = mapper.toEntity(dto);
        document = service.save(document);
        return mapper.toDto(document);
    }

    @Transactional
    public DocumentDTO updateStatus(DocumentDTO dto) {
        Document document = mapper.toEntity(dto);
        document = service.updateStatus(document);
        return mapper.toDto(document);
    }

    @Transactional(readOnly = true)
    public Page<DocumentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Documents");
        return service.findAll(pageable)
            .map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<DocumentDTO> findAllProfessionalDocuments(Pageable pageable, String id) {
        log.debug("Request to get all professional's documents");
        return service.findAllProfessionalDocuments(pageable, id)
            .map(mapper::toDto);
    }


    @Transactional(readOnly = true)
    public DocumentDTO findOne(String id) {
        log.debug("Request to get Document : {}", id);
        Document document = service.findOne(id);
        return mapper.toDto(document);
    }


    public void delete(String id) {
        log.debug("Request to delete Document : {}", id);
        service.delete(id);
    }

    public List<DocumentDTO> getProfessionalDocuments() {
        return mapper.toDto(service.getProfessionalDocuments());
    }

    public Boolean checkIfProfessionalHaveDocumentsWaitingForApprovement() {
        log.debug("Request to check if professional have documents waiting for approvement");
        return service.checkIfProfessionalHaveDocumentsWaitingForApprovement();
    }

    @Transactional
    public void saveDocuments(DocumentsVM documents) {
        List<Document> list = documents.getDocuments().stream().map(mapper::fromDocumentUploadDtoToEntity).collect(Collectors.toList());
        service.saveDocuments(list);
    }
}
