package br.com.homedical.facade.dto.specialty;

import br.com.homedical.domain.enumeration.ObjectStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class SpecialtyProfessionalDTO {

    @NotBlank
    private String id;

    @NotBlank
    private String name;

    @NotNull
    private ObjectStatus status;

}
