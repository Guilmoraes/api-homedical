package br.com.homedical.web.rest;

import br.com.homedical.facade.PatientFacade;
import br.com.homedical.facade.dto.patient.PatientCreateDTO;
import br.com.homedical.facade.dto.patient.PatientDTO;
import br.com.homedical.security.AuthoritiesConstants;
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
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PatientResource {

    private final Logger log = LoggerFactory.getLogger(PatientResource.class);

    private final PatientFacade patientFacade;

    public PatientResource(PatientFacade patientFacade) {
        this.patientFacade = patientFacade;
    }

    @PostMapping("/patients")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN})
    public ResponseEntity<PatientDTO> createPatient(@Valid @RequestBody PatientCreateDTO patientDTO) throws URISyntaxException {
        log.debug("REST request to save Patient : {}", patientDTO);
        return ResponseEntity.ok().body(patientFacade.save(patientDTO));
    }

    @PutMapping("/patients")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN})
    public ResponseEntity<PatientDTO> updatePatient(@Valid @RequestBody PatientDTO patientDTO) throws URISyntaxException {
        log.debug("REST request to update Patient : {}", patientDTO);

        return ResponseEntity.ok().body(patientFacade.update(patientDTO));
    }

    @GetMapping("/patients")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN})
    public ResponseEntity<Page<PatientDTO>> getAllPatients(@RequestParam(value = "query", required = false, defaultValue = "") String query, @ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Patients");
        Page<PatientDTO> page = patientFacade.findAll(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/patients");
        return new ResponseEntity<>(page, headers, HttpStatus.OK);

    }

    @GetMapping("/patients/{id}")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN})
    public ResponseEntity<PatientDTO> getPatient(@PathVariable String id) {
        log.debug("REST request to get Patient : {}", id);
        PatientDTO patientDTO = patientFacade.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(patientDTO));
    }

    @DeleteMapping("/patients/{id}")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN})
    public ResponseEntity<Void> deletePatient(@PathVariable String id) {
        log.debug("REST request to delete Patient : {}", id);

        patientFacade.delete(id);

        return ResponseEntity.ok().build();
    }
}
