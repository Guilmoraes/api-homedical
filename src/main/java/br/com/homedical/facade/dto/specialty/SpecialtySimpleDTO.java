package br.com.homedical.facade.dto.specialty;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class SpecialtySimpleDTO implements Serializable {

    private String id;

    private String name;


}
