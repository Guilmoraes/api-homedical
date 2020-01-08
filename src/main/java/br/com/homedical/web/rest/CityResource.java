package br.com.homedical.web.rest;

import br.com.homedical.security.AuthoritiesConstants;
import br.com.homedical.service.CityService;
import br.com.homedical.service.dto.CityDTO;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CityResource {

    private final Logger log = LoggerFactory.getLogger(CityResource.class);

    private static final String ENTITY_NAME = "city";

    private final CityService cityService;

    public CityResource(CityService cityService) {
        this.cityService = cityService;
    }

    @PostMapping("/cities")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER})
    public ResponseEntity<CityDTO> createCity(@RequestBody CityDTO cityDTO) throws URISyntaxException {
        log.debug("REST request to save City : {}", cityDTO);
        if (cityDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new city cannot already have an ID")).body(null);
        }
        CityDTO result = cityService.save(cityDTO);
        return ResponseEntity.created(new URI("/api/cities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId()))
            .body(result);
    }

    @PutMapping("/cities/{id}")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER})
    public ResponseEntity<CityDTO> updateCity(@RequestBody CityDTO cityDTO, @PathVariable("id") String id) throws URISyntaxException {
        log.debug("REST request to update City : {}", cityDTO);
        if (cityDTO.getId() == null || id == null) {
            return createCity(cityDTO);
        }
        CityDTO result = cityService.update(cityDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cityDTO.getId()))
            .body(result);
    }

    @GetMapping("/cities")
    @Timed
    public ResponseEntity<Page<CityDTO>> getCitiesByName(@RequestParam(value = "query", required = false, defaultValue = "") String query, @ApiParam Pageable pageable) {

        Page<CityDTO> page = cityService.findAllByNameContains(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cities");
        return new ResponseEntity<>(page, headers, HttpStatus.OK);
    }

    @GetMapping("/cities/{id}")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER})
    public ResponseEntity<CityDTO> getCity(@PathVariable String id) {
        log.debug("REST request to get City : {}", id);
        CityDTO cityDTO = cityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cityDTO));
    }

    @DeleteMapping("/cities/{id}")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER})
    public ResponseEntity<Void> deleteCity(@PathVariable String id) {
        log.debug("REST request to delete City : {}", id);
        cityService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
