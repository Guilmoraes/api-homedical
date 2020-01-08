package br.com.homedical.facade.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class DutySimpleDTO implements Serializable {

    @NotBlank
    private String id;

    private String name;

}
