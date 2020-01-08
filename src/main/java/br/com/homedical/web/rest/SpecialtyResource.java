package br.com.homedical.web.rest;

import br.com.homedical.facade.SpecialtyFacade;
import br.com.homedical.facade.dto.specialty.SpecialtyDTO;
import br.com.homedical.facade.dto.specialty.SpecialtyUpdateDTO;
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
public class SpecialtyResource {

    private final Logger log = LoggerFactory.getLogger(SpecialtyResource.class);

    private static final String ENTITY_NAME = "specialty";

    private final SpecialtyFacade specialtyFacade;

    SpecialtyResource(SpecialtyFacade specialtyFacade) {
        this.specialtyFacade = specialtyFacade;
    }

    @PostMapping("/specialties")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN})
    public ResponseEntity<SpecialtyDTO> createSpecialty(@Valid @RequestBody SpecialtyDTO specialtyDTO) throws URISyntaxException {
        SpecialtyDTO result = specialtyFacade.save(specialtyDTO);
        return ResponseEntity.created(new URI("/api/specialties/" + result.getId())).body(result);
    }

    @PutMapping("/specialties")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN})
    public ResponseEntity<SpecialtyDTO> updateSpecialty(@Valid @RequestBody SpecialtyUpdateDTO specialtyUpdateDTO) throws URISyntaxException {
        log.debug("REST request to update Specialty : {}", specialtyUpdateDTO);
        return ResponseEntity.ok().body(specialtyFacade.update(specialtyUpdateDTO));
    }

    @GetMapping("/specialties/enabled")
    @Timed
    public ResponseEntity<List<SpecialtyDTO>> getEnabledSpecialties() {
        log.debug("REST request to get enabled Specialties");
        return ResponseEntity.ok().body(specialtyFacade.getEnabledSpecialties());
    }

    @GetMapping("/specialties")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN})
    public ResponseEntity<Page<SpecialtyDTO>> getAllSpecialties(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Specialties");
        Page<SpecialtyDTO> page = specialtyFacade.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/specialties");
        return new ResponseEntity<>(page, headers, HttpStatus.OK);

    }

    @GetMapping("/specialties/{id}")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN})
    public ResponseEntity<SpecialtyDTO> getSpecialty(@PathVariable String id) {
        log.debug("REST request to get Specialty : {}", id);
        SpecialtyDTO specialtyDTO = specialtyFacade.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(specialtyDTO));
    }
}
