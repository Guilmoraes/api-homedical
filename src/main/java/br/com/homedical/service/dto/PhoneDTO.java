package br.com.homedical.service.dto;


import br.com.homedical.domain.enumeration.PhoneType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class PhoneDTO implements Serializable {

    private String id;

    private Integer areaCode;

    private String number;

    private PhoneType type;

}
