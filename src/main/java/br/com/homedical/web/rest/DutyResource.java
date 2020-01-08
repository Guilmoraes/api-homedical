package br.com.homedical.web.rest;

import br.com.homedical.facade.DutyFacade;
import br.com.homedical.facade.dto.DutyDTO;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class DutyResource {

    private final Logger log = LoggerFactory.getLogger(DutyResource.class);

    private static final String ENTITY_NAME = "duty";

    private final DutyFacade dutyFacade;

    public DutyResource(DutyFacade dutyFacade) {
        this.dutyFacade = dutyFacade;
    }

    @PostMapping("/duties")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN})
    public ResponseEntity<DutyDTO> createDuty(@Valid @RequestBody DutyDTO dutyDTO) throws URISyntaxException {
        log.debug("REST request to save Duty : {}", dutyDTO);
        if (dutyDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new duty cannot already have an ID")).body(null);
        }
        DutyDTO result = dutyFacade.save(dutyDTO);
        return ResponseEntity.created(new URI("/api/duties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId()))
            .body(result);
    }

    @PutMapping("/duties/{id}")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN})
    public ResponseEntity<DutyDTO> updateDuty(@Valid @RequestBody DutyDTO dutyDTO, @PathVariable("id") String id) throws URISyntaxException {
        log.debug("REST request to update Duty : {}", dutyDTO);
        if (dutyDTO.getId() == null || id == null) {
            return createDuty(dutyDTO);
        }
        DutyDTO result = dutyFacade.update(dutyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dutyDTO.getId()))
            .body(result);
    }

    @GetMapping("/duties")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN})
    public ResponseEntity<Page<DutyDTO>> getAllDuties(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Duties");
        Page<DutyDTO> page = dutyFacade.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/duties");
        return new ResponseEntity<>(page, headers, HttpStatus.OK);

    }

    @GetMapping("/duties/{id}")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN})
    public ResponseEntity<DutyDTO> getDuty(@PathVariable String id) {
        log.debug("REST request to get Duty : {}", id);
        DutyDTO dutyDTO = dutyFacade.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(dutyDTO));
    }

    @GetMapping("/duties/listDuties")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN})
    public ResponseEntity<List<DutyDTO>> listDuties() {
        return ResponseEntity.ok().body(dutyFacade.listDuties());
    }

    @DeleteMapping("/duties/{id}")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN})
    public ResponseEntity<Void> deleteDuty(@PathVariable String id) {
        log.debug("REST request to delete Duty : {}", id);
        dutyFacade.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
