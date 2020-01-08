package br.com.homedical.web.rest;

import br.com.homedical.security.AuthoritiesConstants;
import br.com.homedical.service.PhoneService;
import br.com.homedical.service.dto.PhoneDTO;
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
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PhoneResource {

    private final Logger log = LoggerFactory.getLogger(PhoneResource.class);

    private static final String ENTITY_NAME = "phone";

    private final PhoneService phoneService;

    public PhoneResource(PhoneService phoneService) {
        this.phoneService = phoneService;
    }

    @PostMapping("/phones")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER})
    public ResponseEntity<PhoneDTO> createPhone(@RequestBody PhoneDTO phoneDTO) throws URISyntaxException {
        log.debug("REST request to save Phone : {}", phoneDTO);
        if (phoneDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new phone cannot already have an ID")).body(null);
        }
        PhoneDTO result = phoneService.save(phoneDTO);
        return ResponseEntity.created(new URI("/api/phones/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId()))
            .body(result);
    }

    @PutMapping("/phones/{id}")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER})
    public ResponseEntity<PhoneDTO> updatePhone(@RequestBody PhoneDTO phoneDTO, @PathVariable("id") String id) throws URISyntaxException {
        log.debug("REST request to update Phone : {}", phoneDTO);
        if (phoneDTO.getId() == null || id == null) {
            return createPhone(phoneDTO);
        }
        PhoneDTO result = phoneService.update(phoneDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, phoneDTO.getId()))
            .body(result);
    }

    @GetMapping("/phones")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER})
    public ResponseEntity<Page<PhoneDTO>> getAllPhones(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Phones");
        Page<PhoneDTO> page = phoneService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/phones");
        return new ResponseEntity<>(page, headers, HttpStatus.OK);

    }

    @GetMapping("/phones/{id}")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER})
    public ResponseEntity<PhoneDTO> getPhone(@PathVariable String id) {
        log.debug("REST request to get Phone : {}", id);
        PhoneDTO phoneDTO = phoneService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(phoneDTO));
    }

    @DeleteMapping("/phones/{id}")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER})
    public ResponseEntity<Void> deletePhone(@PathVariable String id) {
        log.debug("REST request to delete Phone : {}", id);
        phoneService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
