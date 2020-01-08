package br.com.homedical.service.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
public class CityDTO implements Serializable {

    private String id;

    private String name;

    private Boolean active;

    private StateDTO state;

}
