package br.com.homedical.service.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class AddressDTO implements Serializable {

    private String id;

    private String street;

    private String number;

    private String zipcode;

    private String district;

    private String complement;

    @NotNull
    @Valid
    private CitySimpleDTO city;

    private Boolean isValidAddress;

    private boolean valid = true;

    private Double lat;

    private Double lng;

    private String placeId;

    private String placeName;

    private String placeNameSecondary;

    private String stateName;

    private String cityName;

    private String route;

    @JsonIgnore
    public boolean isValid() {
        if (valid) {
            valid = !(route == null && city == null && stateName == null);
        }
        return valid;
    }

}
