package br.com.homedical.web.rest;

import br.com.homedical.facade.HealthOperatorFacade;
import br.com.homedical.facade.dto.healthOperator.HealthOperatorDTO;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class HealthOperatorResource {

    private static final String ENTITY_NAME = "healthOperator";
    private final Logger log = LoggerFactory.getLogger(HealthOperatorResource.class);
    private final HealthOperatorFacade healthOperatorFacade;

    public HealthOperatorResource(HealthOperatorFacade healthOperatorFacade) {
        this.healthOperatorFacade = healthOperatorFacade;
    }

    @PostMapping("/health-operators")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER})
    public ResponseEntity<HealthOperatorDTO> createHealthOperator(@Valid @RequestBody HealthOperatorDTO healthOperatorDTO) throws URISyntaxException {
        log.debug("REST request to save HealthOperator : {}", healthOperatorDTO);
        if (healthOperatorDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new healthOperator cannot already have an ID")).body(null);
        }
        HealthOperatorDTO result = healthOperatorFacade.save(healthOperatorDTO);
        return ResponseEntity.created(new URI("/api/health-operators/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId()))
            .body(result);
    }

    @PutMapping("/health-operators/{id}")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER})
    public ResponseEntity<HealthOperatorDTO> updateHealthOperator(@Valid @RequestBody HealthOperatorDTO healthOperatorDTO, @PathVariable("id") String id) throws URISyntaxException {
        log.debug("REST request to update HealthOperator : {}", healthOperatorDTO);
        if (healthOperatorDTO.getId() == null || id == null) {
            return createHealthOperator(healthOperatorDTO);
        }
        HealthOperatorDTO result = healthOperatorFacade.update(healthOperatorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, healthOperatorDTO.getId()))
            .body(result);
    }

    @GetMapping("/health-operators")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN})
    public ResponseEntity<Page<HealthOperatorDTO>> getAllHealthOperators(@RequestParam(value = "query", required = false, defaultValue = "") String query, @ApiParam Pageable pageable) {
        log.debug("REST request to get a page of HealthOperators");
        Page<HealthOperatorDTO> page = healthOperatorFacade.findAll(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/health-operators");
        return new ResponseEntity<>(page, headers, HttpStatus.OK);

    }

    @GetMapping("/health-operators/{id}")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER})
    public ResponseEntity<HealthOperatorDTO> getHealthOperator(@PathVariable String id) {
        log.debug("REST request to get HealthOperator : {}", id);
        HealthOperatorDTO healthOperatorDTO = healthOperatorFacade.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(healthOperatorDTO));
    }

    @DeleteMapping("/health-operators/{id}")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER})
    public ResponseEntity<Void> deleteHealthOperator(@PathVariable String id) {
        log.debug("REST request to delete HealthOperator : {}", id);
        healthOperatorFacade.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
