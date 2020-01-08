package br.com.homedical.facade.dto.professional;


import br.com.homedical.service.dto.PhoneDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class ProfessionalReportDTO implements Serializable {

    private String id;

    @NotNull
    private String name;

    @NotBlank
    private String cpf;

    @NotNull
    @Valid
    private PhoneDTO phone;
}
