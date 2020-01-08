package br.com.homedical.web.rest;

import br.com.homedical.domain.Installation;
import br.com.homedical.service.InstallationService;
import br.com.homedical.web.rest.util.HeaderUtil;
import br.com.homedical.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
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

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static br.com.homedical.security.AuthoritiesConstants.ADMIN;
import static br.com.homedical.security.AuthoritiesConstants.PROFESSIONAL;
import static br.com.homedical.security.AuthoritiesConstants.USER;

@RestController
@RequestMapping("/api")
public class InstallationResource {

    private final Logger log = LoggerFactory.getLogger(InstallationResource.class);

    @Inject
    private InstallationService installationService;

    @PostMapping("/installations")
    @Timed
    @Secured({ADMIN, USER, PROFESSIONAL})
    public ResponseEntity<Installation> createInstallation(@Valid @RequestBody Installation installation) throws URISyntaxException {
        log.debug("REST request to saveCheckRegion Installation : {}", installation);
        Installation result = installationService.save(installation);
        return ResponseEntity.created(new URI("/api/installations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("installation", result.getId()))
            .body(result);
    }

    @PutMapping("/installations")
    @Secured({ADMIN, USER, PROFESSIONAL})
    public ResponseEntity<Installation> updateInstallation(@Valid @RequestBody Installation installation) throws URISyntaxException {
        log.debug("REST request to updateByCustomerRequest Installation : {}", installation);
        if (installation.getId() == null) {
            return createInstallation(installation);
        }
        Installation result = installationService.save(installation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("installation", installation.getId()))
            .body(result);
    }

    @GetMapping("/installations")
    @Secured({ADMIN, USER})
    public ResponseEntity<List<Installation>> getAllInstallations(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Installations");
        Page<Installation> page = installationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/installations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/installations/{id}")
    @Secured({ADMIN, USER, PROFESSIONAL})
    public ResponseEntity<Installation> getInstallation(@PathVariable String id) {
        log.debug("REST request to get Installation : {}", id);
        Installation installation = installationService.findOne(id);
        return Optional.ofNullable(installation)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/installations/{id}")
    @Secured({ADMIN, USER})
    public ResponseEntity<Void> deleteInstallation(@PathVariable String id) {
        log.debug("REST request to delete Installation : {}", id);
        installationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("installation", id)).build();
    }

    @DeleteMapping("/installations/tokens/{token}")
    @Secured({ADMIN, USER})
    public ResponseEntity<Void> deleteInstallationByDeviceToken(@PathVariable String token) {
        log.debug("REST request to delete Installation by token : {}", token);
        installationService.removeInstallationsForDeviceToken(token);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("installation", token)).build();
    }

}
