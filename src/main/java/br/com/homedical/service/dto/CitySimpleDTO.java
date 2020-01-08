package br.com.homedical.service.dto;


import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
public class CitySimpleDTO implements Serializable {

    @NotBlank
    private String id;

    @NotBlank
    private String name;


}
