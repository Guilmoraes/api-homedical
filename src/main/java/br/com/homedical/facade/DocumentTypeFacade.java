package br.com.homedical.facade;

import br.com.homedical.domain.DocumentType;
import br.com.homedical.facade.dto.documentType.DocumentTypeCreateDTO;
import br.com.homedical.facade.dto.documentType.DocumentTypeDTO;
import br.com.homedical.facade.dto.documentType.DocumentTypeSimpleDTO;
import br.com.homedical.facade.mapper.DocumentTypeMapper;
import br.com.homedical.service.DocumentTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class DocumentTypeFacade {

    private final Logger log = LoggerFactory.getLogger(DocumentTypeFacade.class);

    private final DocumentTypeService documentTypeService;

    private final DocumentTypeMapper documentTypeMapper;

    public DocumentTypeFacade(DocumentTypeService documentTypeService,
                              DocumentTypeMapper documentTypeMapper) {
            this.documentTypeService = documentTypeService;
            this.documentTypeMapper = documentTypeMapper;
    }

    @Transactional
    public DocumentTypeDTO save(DocumentTypeCreateDTO documentTypeCreateDTO) {
        log.debug("Request to save DocumentType : {}",documentTypeCreateDTO);
        DocumentType documentType = documentTypeMapper.fromDocumentTypeCreateDtoToEntity(documentTypeCreateDTO);
        documentType = documentTypeService.save(documentType);
        return documentTypeMapper.toDto(documentType);
    }

    @Transactional
    public DocumentTypeDTO update(DocumentTypeDTO documentTypeDTO) {
        log.debug("Request to update DocumentType : {}", documentTypeDTO);
        DocumentType documentType = documentTypeMapper.toEntity(documentTypeDTO);
        documentType = documentTypeService.save(documentType);
        return documentTypeMapper.toDto(documentType);
    }

    @Transactional(readOnly = true)
    public Page<DocumentTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DocumentTypes");
        return documentTypeService.findAll(pageable)
            .map(documentTypeMapper::toDto);
    }


    @Transactional(readOnly = true)
    public DocumentTypeDTO findOne(String id) {
        log.debug("Request to get DocumentType : {}", id);
        DocumentType documentType = documentTypeService.findOne(id);
        return documentTypeMapper.toDto(documentType);
    }


    public void delete(String id) {
        log.debug("Request to delete DocumentType : {}", id);
        documentTypeService.delete(id);
    }

    public List<DocumentTypeSimpleDTO> getPendingDocumentTypesForProfessional() {
        log.debug("Request to get Pending document types for professional");
        return documentTypeMapper.fromEntityListToSimpleList(documentTypeService.getPendingDocumentTypesForProfessional());
    }
}
