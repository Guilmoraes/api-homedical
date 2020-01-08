package br.com.homedical.web.rest;

import br.com.homedical.domain.Professional;
import br.com.homedical.facade.ProfessionalFacade;
import br.com.homedical.facade.dto.DutyDTO;
import br.com.homedical.facade.dto.DutySimpleDTO;
import br.com.homedical.facade.dto.patient.PatientDTO;
import br.com.homedical.facade.dto.patient.PatientIdDTO;
import br.com.homedical.facade.dto.professional.ProfessionalDTO;
import br.com.homedical.facade.dto.professional.ProfessionalRegisterByAdminDTO;
import br.com.homedical.facade.dto.professional.ProfessionalUpdateDTO;
import br.com.homedical.security.AuthoritiesConstants;
import br.com.homedical.security.jwt.TokenProvider;
import br.com.homedical.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProfessionalResource {

    private static final String ENTITY_NAME = "professional";

    private final Logger log = LoggerFactory.getLogger(ProfessionalResource.class);

    private final ProfessionalFacade professionalFacade;

    private final TokenProvider tokenProvider;

    public ProfessionalResource(ProfessionalFacade professionalFacade,
                                TokenProvider tokenProvider) {
        this.professionalFacade = professionalFacade;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/professionals")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN})
    public ResponseEntity<ProfessionalDTO> createProfessional(@Valid @RequestBody ProfessionalRegisterByAdminDTO professionalRegisterByAdminDTO) throws URISyntaxException {
        log.debug("REST request to save Professional : {}", professionalRegisterByAdminDTO);

        ProfessionalDTO professional = professionalFacade.save(professionalRegisterByAdminDTO);

        return ResponseEntity.ok(professional);
    }

    @GetMapping("/professionals/search")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN})
    public ResponseEntity<List<ProfessionalDTO>> searchProfessional(@RequestParam(name = "firstName", required = false) String name) {
        log.debug("Request search for professional");
        return ResponseEntity.ok().body(professionalFacade.searchProfessional(name));
    }

    @PutMapping("/professionals")
    @Timed
    @Secured({AuthoritiesConstants.PROFESSIONAL})
    public ResponseEntity updateProfessional(@Valid @RequestBody ProfessionalUpdateDTO professionalUpdateDTO) throws URISyntaxException {
        log.debug("REST request to update Professional : {}", professionalUpdateDTO);

        Professional professional = professionalFacade.update(professionalUpdateDTO);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-NEW-TOKEN", tokenProvider.createToken(professional.getUser().getEmail(), professional.getUser().getAuthorities(), true));
        return ResponseEntity.ok().headers(httpHeaders).build();
    }

    @GetMapping("/professionals")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN})
    public ResponseEntity<Page<ProfessionalDTO>> getAllProfessionals(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Professionals");
        Page<ProfessionalDTO> page = professionalFacade.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/professionals");
        return new ResponseEntity<>(page, headers, HttpStatus.OK);
    }

    @GetMapping("/professionals/searchlist")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN})
    public ResponseEntity<Page<ProfessionalDTO>> searchListProfessionals(@ApiParam Pageable pageable, @RequestParam String name) {
        log.debug("REST request to get a page of Professionals");
        Page<ProfessionalDTO> page = professionalFacade.findAllByName(pageable, name);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/professionals/searchlist");
        return new ResponseEntity<>(page, headers, HttpStatus.OK);
    }

    @GetMapping("/professionals/{id}")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN})
    public ResponseEntity<ProfessionalDTO> getProfessional(@PathVariable String id) {
        log.debug("REST request to get Professional : {}", id);

        ProfessionalDTO professionalDTO = professionalFacade.findOne(id);

        return ResponseEntity.ok().body(professionalDTO);
    }

    @GetMapping("/professionals/me")
    @Timed
    @Secured({AuthoritiesConstants.PROFESSIONAL})
    public ResponseEntity<ProfessionalDTO> getMe() {
        log.debug("REST request to get Professional self information.");

        return ResponseEntity.ok().body(professionalFacade.getMe());
    }

    @PutMapping("/professionals/{id}/patients")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN})
    public ResponseEntity<List<PatientDTO>> updateProfessionalPatients(@PathVariable String id, @RequestBody @Valid List<PatientIdDTO> patients) {
        return ResponseEntity.ok().body(professionalFacade.updateProfessionalPatients(id, patients));
    }

    @PutMapping("/professionals/{id}/duties")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN})
    public ResponseEntity<List<DutyDTO>> updateProfessionalsDuties(@PathVariable String id, @RequestBody @Valid List<DutySimpleDTO> duties) {
        return ResponseEntity.ok().body(professionalFacade.updateProfessionalDuites(id, duties));
    }

    @GetMapping("/professionals/{id}/duties")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.PROFESSIONAL})
    public ResponseEntity<List<DutyDTO>> listProfessionalDuties(@PathVariable String id) {
        return ResponseEntity.ok().body(professionalFacade.listProfessionalWitchDuty(id));
    }

    @GetMapping("/professionals/{id}/patients")
    @Timed
    @Secured({AuthoritiesConstants.PROFESSIONAL, AuthoritiesConstants.ADMIN})
    public ResponseEntity<List<PatientDTO>> getProfessionalPatients(@PathVariable String id) {
        return ResponseEntity.ok().body(professionalFacade.getProfessionalPatients(id));
    }

    @GetMapping("/professionals/documents")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN})
    public ResponseEntity<Page<ProfessionalDTO>> getProfessionalDocuments(@ApiParam Pageable pageable) {
        Page<ProfessionalDTO> page = professionalFacade.getProfessionalDocuments(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/professionals/documents");
        return new ResponseEntity<>(page, headers, HttpStatus.OK);
    }
}
