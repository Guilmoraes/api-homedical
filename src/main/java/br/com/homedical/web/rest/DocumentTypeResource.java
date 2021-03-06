package br.com.homedical.web.rest;

import br.com.homedical.facade.DocumentTypeFacade;
import br.com.homedical.facade.dto.documentType.DocumentTypeCreateDTO;
import br.com.homedical.facade.dto.documentType.DocumentTypeDTO;
import br.com.homedical.facade.dto.documentType.DocumentTypeSimpleDTO;
import br.com.homedical.security.AuthoritiesConstants;
import br.com.homedical.web.rest.util.HeaderUtil;
import br.com.homedical.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class DocumentTypeResource {

    private final Logger log = LoggerFactory.getLogger(DocumentTypeResource.class);

    private static final String ENTITY_NAME = "documentType";

    private final DocumentTypeFacade documentTypeFacade;

    public DocumentTypeResource(DocumentTypeFacade documentTypeFacade) {
        this.documentTypeFacade = documentTypeFacade;
    }

    @PostMapping("/document-types")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN})
    public ResponseEntity<DocumentTypeDTO> createDocumentType(@RequestBody DocumentTypeCreateDTO documentTypeCreateDTO) throws URISyntaxException {
        log.debug("REST request to save DocumentType : {}", documentTypeCreateDTO);
        if (documentTypeCreateDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new documentType cannot already have an ID")).body(null);
        }
        DocumentTypeDTO result = documentTypeFacade.save(documentTypeCreateDTO);
        return ResponseEntity.created(new URI("/api/document-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId()))
            .body(result);
    }

    @PutMapping("/document-types/{id}")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN})
    public ResponseEntity<DocumentTypeDTO> updateDocumentType(@RequestBody DocumentTypeDTO documentTypeDTO, @PathVariable("id") String id) throws URISyntaxException {
        log.debug("REST request to update DocumentType : {}", documentTypeDTO);
        DocumentTypeDTO result = documentTypeFacade.update(documentTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, documentTypeDTO.getId()))
            .body(result);
    }

    @GetMapping("/document-types")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER})
    public ResponseEntity<Page<DocumentTypeDTO>> getAllDocumentTypes(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of DocumentTypes");
        Page<DocumentTypeDTO> page = documentTypeFacade.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/document-types");
        return new ResponseEntity<>(page, headers, HttpStatus.OK);

    }

    @GetMapping("/document-types/{id}")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER})
    public ResponseEntity<DocumentTypeDTO> getDocumentType(@PathVariable String id) {
        log.debug("REST request to get DocumentType : {}", id);
        DocumentTypeDTO documentTypeDTO = documentTypeFacade.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(documentTypeDTO));
    }

    @DeleteMapping("/document-types/{id}")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN})
    public ResponseEntity<Void> deleteDocumentType(@PathVariable String id) {
        log.debug("REST request to delete DocumentType : {}", id);
        documentTypeFacade.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    @GetMapping("/document-types/pending")
    @Timed
    @Secured({AuthoritiesConstants.PROFESSIONAL})
    public ResponseEntity<List<DocumentTypeSimpleDTO>> getPendingDocumentTypesForProfessional() {
        log.debug("REST request to get pending document types for professional");
        return ResponseEntity.ok(documentTypeFacade.getPendingDocumentTypesForProfessional());
    }
}
