package br.com.homedical.web.rest;

import br.com.homedical.security.AuthoritiesConstants;
import br.com.homedical.service.StateService;
import br.com.homedical.service.dto.StateDTO;
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
public class StateResource {

    private final Logger log = LoggerFactory.getLogger(StateResource.class);

    private static final String ENTITY_NAME = "state";

    private final StateService stateService;

    public StateResource(StateService stateService) {
        this.stateService = stateService;
    }

    @PostMapping("/states")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER})
    public ResponseEntity<StateDTO> createState(@RequestBody StateDTO stateDTO) throws URISyntaxException {
        log.debug("REST request to save State : {}", stateDTO);
        if (stateDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new state cannot already have an ID")).body(null);
        }
        StateDTO result = stateService.save(stateDTO);
        return ResponseEntity.created(new URI("/api/states/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId()))
            .body(result);
    }

    @PutMapping("/states/{id}")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER})
    public ResponseEntity<StateDTO> updateState(@RequestBody StateDTO stateDTO, @PathVariable("id") String id) throws URISyntaxException {
        log.debug("REST request to update State : {}", stateDTO);
        if (stateDTO.getId() == null || id == null) {
            return createState(stateDTO);
        }
        StateDTO result = stateService.update(stateDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stateDTO.getId()))
            .body(result);
    }

    @GetMapping("/states")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER})
    public ResponseEntity<Page<StateDTO>> getAllStates(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of States");
        Page<StateDTO> page = stateService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/states");
        return new ResponseEntity<>(page, headers, HttpStatus.OK);

    }

    @GetMapping("/states/{id}")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER})
    public ResponseEntity<StateDTO> getState(@PathVariable String id) {
        log.debug("REST request to get State : {}", id);
        StateDTO stateDTO = stateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stateDTO));
    }

    @DeleteMapping("/states/{id}")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER})
    public ResponseEntity<Void> deleteState(@PathVariable String id) {
        log.debug("REST request to delete State : {}", id);
        stateService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
