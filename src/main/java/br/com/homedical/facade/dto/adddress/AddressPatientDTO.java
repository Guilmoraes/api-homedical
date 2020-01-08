package br.com.homedical.facade.dto.adddress;

import br.com.homedical.service.dto.CitySimpleDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class AddressPatientDTO {

    private String id;

    private String street;

    private String number;

    private String zipcode;

    private String district;

    private String complement;

    private CitySimpleDTO city;

    private Boolean isValidAddress;

    private boolean valid = true;

    @NotNull
    private Double lat;

    @NotNull
    private Double lng;

    private String placeId;

    private String placeName;

    private String stateName;

    private String cityName;

    private String route;
}
