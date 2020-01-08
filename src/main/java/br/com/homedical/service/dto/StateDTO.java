package br.com.homedical.service.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class StateDTO implements Serializable {

    private String id;

    private String name;

    private String acronym;

    private Boolean active;

    private CountrySimpleDTO country;

}
