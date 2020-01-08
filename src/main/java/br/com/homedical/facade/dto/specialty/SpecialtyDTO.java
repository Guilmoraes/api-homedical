package br.com.homedical.facade.dto.specialty;


import br.com.homedical.domain.enumeration.ObjectStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class SpecialtyDTO implements Serializable {

    private String id;

    @NotBlank
    private String name;

    @NotNull
    private ObjectStatus status;


}
