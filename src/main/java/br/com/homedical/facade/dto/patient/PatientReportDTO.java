package br.com.homedical.facade.dto.patient;


import br.com.homedical.service.dto.AddressDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class PatientReportDTO implements Serializable {

    @NotBlank
    private String id;

    private String name;

    @Valid
    private AddressDTO address;

}
