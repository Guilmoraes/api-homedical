package br.com.homedical.service.mapper;

import br.com.homedical.service.dto.AddressDTO;
import com.google.common.base.Preconditions;
import com.google.maps.model.AutocompletePrediction;
import com.google.maps.model.PlaceDetails;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AddressGeocodingMapper {

    public static List<AddressDTO> toDTOList(List<PlaceDetails> detailsList) {
        Preconditions.checkNotNull(detailsList, "GeocodingResult list cannot be null");
        List<AddressDTO> list = new ArrayList<>();

        for (PlaceDetails geocodingResult : detailsList) {
            list.add(toDTO(geocodingResult));
        }

        return list;
    }

    public static List<AddressDTO> toDTOList(AutocompletePrediction[] detailsList) {
        Preconditions.checkNotNull(detailsList, "GeocodingResult list cannot be null");
        List<AddressDTO> list = new ArrayList<>();

        for (AutocompletePrediction geocodingResult : detailsList) {
            list.add(toDTOPrediction(geocodingResult));
        }

        return list.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    private static AddressDTO toDTOPrediction(AutocompletePrediction details) {

        Preconditions.checkNotNull(details, "GeocodingResult cannot be null");

        AddressDTO dto = new AddressDTO();
        dto.setPlaceId(details.placeId);
        dto.setPlaceName(details.structuredFormatting.mainText);
        dto.setPlaceNameSecondary(details.structuredFormatting.secondaryText);

        List<AutocompletePrediction.Term> terms = Arrays.asList(details.terms);
        Collections.reverse(terms);

        if (terms.size() > 1) {
            if (!StringUtils.containsIgnoreCase(terms.get(0).value, "brasil") && !StringUtils.containsIgnoreCase(terms.get(0).value, "brazil")) {
                return null;
            }
        }
        List<AutocompletePrediction.Term> collect = terms
            .stream()
            .skip(1)
            .collect(Collectors.toList());

        if (collect.size() < 2) {
            return null;
        }

        dto.setStateName(collect.get(0).value);
        dto.setCityName(collect.get(1).value);

        return dto;
    }

    public static AddressDTO toDTO(PlaceDetails details) {
        Preconditions.checkNotNull(details, "GeocodingResult cannot be null");

        AddressDTO dto = new AddressDTO();
        dto.setPlaceId(details.placeId);
        dto.setPlaceName(details.name);

        if (details.geometry != null) {
            dto.setLat(details.geometry.location.lat);
            dto.setLng(details.geometry.location.lng);
        }

        Arrays.stream(details.addressComponents).forEach(
            c -> Arrays.stream(c.types).forEach(
                type -> {
                    switch (type) {
                        case POSTAL_CODE:
                            String zipcode = c.longName.replaceAll("[^a-zA-Z0-9]", "");
                            dto.setZipcode(zipcode);
                            break;
                        case ADMINISTRATIVE_AREA_LEVEL_1:
                            dto.setStateName(c.shortName);
                            break;
                        case ADMINISTRATIVE_AREA_LEVEL_2:
                            dto.setCityName(c.longName);
                            break;
                        case ROUTE:
                            dto.setRoute(c.longName);
                            dto.setStreet(c.longName);
                            break;
                        case STREET_NUMBER:
                            dto.setNumber(c.longName);
                            break;
                    }
                }
            )
        );

        return dto;
    }
}
