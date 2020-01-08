package br.com.homedical.web.rest;

import br.com.homedical.service.GeocoderService;
import br.com.homedical.service.dto.AddressDTO;
import br.com.homedical.service.mapper.AddressGeocodingMapper;
import br.com.homedical.util.StreamUtils;
import com.codahale.metrics.annotation.Timed;
import com.google.maps.errors.InvalidRequestException;
import com.google.maps.model.PlaceDetails;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by gmribas on 22/08/17.
 */
@RestController
@RequestMapping("/api")
public class AddressGeocodingResource {

    private final Logger log = LoggerFactory.getLogger(AddressGeocodingResource.class);

    private final GeocoderService geocoderService;

    public AddressGeocodingResource(GeocoderService geocoderService) {
        this.geocoderService = geocoderService;
    }

    @GetMapping(path = "/address-geocoder")
    @Timed
    public ResponseEntity<List<AddressDTO>> searchAddress(@RequestParam(value = "q", required = false) String address, @RequestParam(value = "origin", required = false) Boolean origin, @RequestParam(value = "lat", required = false) Double latitude, @RequestParam(value = "lng", required = false) Double longitude) {
        log.debug("REST request to search geocoder address : {}", address);

        if (address == null || address.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        List<AddressDTO> dtoList = null;

        List<PlaceDetails> results = geocoderService.searchAddress(address, latitude, longitude, origin);

        if (results != null && results.size() > 0) {
            dtoList = AddressGeocodingMapper.toDTOList(results);
            dtoList = dtoList
                .stream()
                .filter(AddressDTO::isValid)
                .filter(it -> it.getPlaceId() != null)
                .filter(StreamUtils.distinctByKey(AddressDTO::getPlaceId))
                .filter(dto -> !dto.getCityName().isEmpty())
                .collect(Collectors.toList());

            if (dtoList.isEmpty()) dtoList = null;
        }

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(dtoList));
    }

    @GetMapping(path = "/place-id")
    @Timed
    public ResponseEntity<AddressDTO> searchPlaceId(@RequestParam(value = "q", required = false) String placeId) {
        log.debug("REST request to search place id : {}", placeId);

        if (placeId == null || placeId.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        AddressDTO dto = null;
        try {
            PlaceDetails result = geocoderService.findByPlaceId(placeId);
            if (result != null) {
                dto = AddressGeocodingMapper.toDTO(result);
            }

        } catch (Exception e) {
            if (e.getCause() != null && e.getCause() instanceof InvalidRequestException) {
                return ResponseUtil.wrapOrNotFound(Optional.empty());
            }
            throw e;
        }

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(dto));
    }
}
