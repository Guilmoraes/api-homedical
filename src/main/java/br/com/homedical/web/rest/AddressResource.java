package br.com.homedical.web.rest;

import br.com.homedical.security.AuthoritiesConstants;
import br.com.homedical.service.AddressService;
import br.com.homedical.service.dto.AddressDTO;
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
public class AddressResource {

    private final Logger log = LoggerFactory.getLogger(AddressResource.class);

    private static final String ENTITY_NAME = "address";

    private final AddressService addressService;

    public AddressResource(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping("/addresses")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER})
    public ResponseEntity<AddressDTO> createAddress(@RequestBody AddressDTO addressDTO) throws URISyntaxException {
        log.debug("REST request to save Address : {}", addressDTO);
        if (addressDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new address cannot already have an ID")).body(null);
        }
        AddressDTO result = addressService.save(addressDTO);
        return ResponseEntity.created(new URI("/api/addresses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId()))
            .body(result);
    }

    @PutMapping("/addresses/{id}")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER})
    public ResponseEntity<AddressDTO> updateAddress(@RequestBody AddressDTO addressDTO, @PathVariable("id") String id) throws URISyntaxException {
        log.debug("REST request to update Address : {}", addressDTO);
        if (addressDTO.getId() == null || id == null) {
            return createAddress(addressDTO);
        }
        AddressDTO result = addressService.update(addressDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, addressDTO.getId()))
            .body(result);
    }

    @GetMapping("/addresses")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER})
    public ResponseEntity<Page<AddressDTO>> getAllAddresses(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Addresses");
        Page<AddressDTO> page = addressService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/addresses");
        return new ResponseEntity<>(page, headers, HttpStatus.OK);

    }

    @GetMapping("/addresses/{id}")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER})
    public ResponseEntity<AddressDTO> getAddress(@PathVariable String id) {
        log.debug("REST request to get Address : {}", id);
        AddressDTO addressDTO = addressService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(addressDTO));
    }

    @DeleteMapping("/addresses/{id}")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER})
    public ResponseEntity<Void> deleteAddress(@PathVariable String id) {
        log.debug("REST request to delete Address : {}", id);
        addressService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
