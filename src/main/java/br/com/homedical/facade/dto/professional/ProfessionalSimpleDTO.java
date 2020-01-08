package br.com.homedical.facade.dto.professional;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ProfessionalSimpleDTO implements Serializable {

    @NotBlank
    private String id;

    private String name;
}
