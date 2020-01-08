package br.com.homedical.service.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class CountrySimpleDTO implements Serializable {

    private String id;

    private String name;


}
