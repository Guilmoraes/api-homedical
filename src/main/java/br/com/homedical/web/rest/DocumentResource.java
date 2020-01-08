package br.com.homedical.web.rest;

import br.com.homedical.facade.DocumentFacade;
import br.com.homedical.facade.dto.document.DocumentDTO;
import br.com.homedical.facade.dto.document.DocumentUploadDTO;
import br.com.homedical.security.AuthoritiesConstants;
import br.com.homedical.web.rest.util.HeaderUtil;
import br.com.homedical.web.rest.util.PaginationUtil;
import br.com.homedical.web.rest.vm.DocumentsVM;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class DocumentResource {

    private final Logger log = LoggerFactory.getLogger(DocumentResource.class);

    private static final String ENTITY_NAME = "document";

    private final DocumentFacade documentFacade;


    public DocumentResource(DocumentFacade documentFacade) {
        this.documentFacade = documentFacade;
    }

    @PostMapping("/documents")
    @Timed
    @Secured({AuthoritiesConstants.PROFESSIONAL})
    public ResponseEntity<DocumentDTO> createDocument(@Valid @RequestBody DocumentUploadDTO documentUploadDTO) throws URISyntaxException {
        log.debug("REST request to save Document : {}", documentUploadDTO);

        DocumentDTO result = documentFacade.save(documentUploadDTO);

        return ResponseEntity.created(new URI("/api/documents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId()))
            .body(result);
    }

    @PostMapping("/documents/list")
    @Timed
    @Secured({AuthoritiesConstants.PROFESSIONAL})
    public ResponseEntity<Void> createDocuments(@Valid @RequestBody DocumentsVM documents) {

        documentFacade.saveDocuments(documents);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/documents/status")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN})
    public ResponseEntity<DocumentDTO> updateStatusDocument(@RequestBody DocumentDTO documentDTO) throws URISyntaxException {
        DocumentDTO result = documentFacade.updateStatus(documentDTO);
        return ResponseEntity.ok()
            .body(result);
    }

    @GetMapping("/documents")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER})
    public ResponseEntity<Page<DocumentDTO>> getAllDocuments(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Documents");
        Page<DocumentDTO> page = documentFacade.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/documents");
        return new ResponseEntity<>(page, headers, HttpStatus.OK);

    }

    @GetMapping("/documents/{id}/professionals")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN})
    public ResponseEntity<Page<DocumentDTO>> getAllProfessionalDocuments(@PathVariable String id, @ApiParam Pageable pageable) {
        log.debug("REST request to get all profissional documents : {}", id);
        Page<DocumentDTO> page = documentFacade.findAllProfessionalDocuments(pageable, id);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/documents/professionals");
        return new ResponseEntity<>(page, headers, HttpStatus.OK);
    }

    @GetMapping("/documents/professional")
    @Timed
    @Secured({AuthoritiesConstants.PROFESSIONAL})
    public ResponseEntity<List<DocumentDTO>> getProfessionalDocuments() {
        return ResponseEntity.ok().body(documentFacade.getProfessionalDocuments());
    }

    @GetMapping("/documents/{id}")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER})
    public ResponseEntity<DocumentDTO> getDocument(@PathVariable String id) {
        log.debug("REST request to get Document : {}", id);
        DocumentDTO documentDTO = documentFacade.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(documentDTO));
    }

    @DeleteMapping("/documents/{id}")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER})
    public ResponseEntity<Void> deleteDocument(@PathVariable String id) {
        log.debug("REST request to delete Document : {}", id);
        documentFacade.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    @GetMapping("/documents/professional/is-waiting-approvement")
    @Timed
    @Secured({AuthoritiesConstants.PROFESSIONAL})
    public ResponseEntity<Boolean> checkIfProfessionalHaveDocumentsWaitingForApprovement() {
        log.debug("REST request to check if professional have documents waiting for approvement");
        return ResponseEntity.ok(documentFacade.checkIfProfessionalHaveDocumentsWaitingForApprovement());
    }
}
